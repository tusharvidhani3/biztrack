import { useCallback, useEffect, useReducer, useRef, useState } from "react"
import Searchbox from "./Searchbox"
import { useAuthFetch } from "../hooks/useAuthFetch"
import { apiBaseUrl } from "../config"
import { useSearchParams } from "react-router"
import AccountsFilterMenu from "./AccountsFilterMenu"

export default function Accounts() {

    const [searchParams, setSearchParams] = useSearchParams()
    const [ keyword, setKeyword ] = useState(searchParams.get('k') || '')
    const [accounts, setAccounts] = useState(null)
    const authFetch = useAuthFetch()
    
    const handleSearchSubmit = useCallback(() => {
        setSearchParams(prev => {
            const params = new URLSearchParams(prev)
            params.set('k', keyword)
            return params
        })
    }, [])

    const searchAccounts = useCallback(async () => {
        const res = await authFetch(`${apiBaseUrl}/api/accounts`, {
            method: 'GET'
        })
        const accountsList = await res.json()
        setAccounts(accountsList)
    }, [])
    
    useEffect(() => {
        const init = async () => await searchAccounts()
        init()
    }, [])

    return (
        <>
            <section>
                <div>
                    <div></div>
                    <div>Total Amount Due from all customers</div>
                </div>

                <div>
                    <div></div>
                    <div>Total Amount to pay to all suppliers</div>
                </div>
            </section>
            <section>
                <Searchbox keyword={keyword} setKeyword={setKeyword} handleSubmit={handleSearchSubmit} placeholder={"Search by party"} />
                <AccountsFilterMenu searchParams={searchParams} setSearchParams={setSearchParams} />
            </section>
            <section>
                {
                    accounts.map(account => {
                        <div className="flex flex-col rounded-xl border">
                            <div className="font-bold">{account.party.name} {account.party.city}</div>
                            <div>Outstanding balance: <span className="font-bold">{account.totalDue}</span></div>
                            <div>L. Pay Date: <span>{account.lastPaymentDate}</span></div>
                            <div>Party Type: <span className="font-bold">{account.party.type}</span></div>
                        </div>
                    })
                }
            </section>            
        </>
    )
}
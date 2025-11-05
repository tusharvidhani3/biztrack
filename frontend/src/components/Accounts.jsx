import { useCallback, useEffect, useMemo, useReducer, useRef, useState } from "react"
import Searchbox from "./Searchbox"
import { useAuthFetch } from "../hooks/useAuthFetch"
import { apiBaseUrl } from "../config"

export default function Accounts() {

    const [keyword, setKeyword] = useState('')
    const [accounts, setAccounts] = useState(null)
    const authFetch = useAuthFetch()
    const paymentEntryReducer = useCallback((state, action) => {
        const { type, payload } = action
        switch (type) {
            case 'AMOUNT':
                return { ...state, amount: payload }
            case 'DATE':
                return { ...state, date: payload }
        }
    }, [])

    const dateInputRef = useRef(null)

    const dateToday = useMemo(() => new Date(), [])

    const [paymentEntry, dispatch] = useReducer(paymentEntryReducer, {
        amount: '',
        date: `${dateToday.toISOString().split("T")[0]}`
    })

    const dateLabel = useMemo(() => {
        const selectedDate = new Date(paymentEntry.date)
        if (selectedDate.getDate() === dateToday.getDate() && selectedDate.getMonth() === dateToday.getMonth() && selectedDate.getFullYear() === dateToday.getFullYear())
            return 'Today'
        dateToday.setDate(dateToday.getDate() - 1)
        if (selectedDate.getDate() === dateToday.getDate() && selectedDate.getMonth() === dateToday.getMonth() && selectedDate.getFullYear() === dateToday.getFullYear())
            return 'Yesterday'
        else
            return paymentEntry.date
    }, [dateToday, paymentEntry])

    const searchAccounts = useCallback(async () => {
        const res = await authFetch(`${apiBaseUrl}/api/accounts`, {
            method: 'GET'
        })
        const filters = []
        if(keyword)
            filters.push(`keyword=${keyword}`)
        if(partyType)
            filters.push(`partyType=${partyType}`)
        if(places.length) {
            places.forEach(place => {
                filters.push(`places=${place}`)
            })
        }
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
            <Searchbox keyword={keyword} setKeyword={setKeyword} placeholder={"Search by party"} />
            <section>
                {
                    accounts.map(account => {
                        <div className="flex flex-col rounded-xl border">
                            <div className="font-bold">{account.party.name} {account.party.city}</div>
                            <div>Outstanding balance: <span className="font-bold">{account.totalDue}</span></div>
                            <div>L. Pay Date: <span></span></div>
                            <div>Party Type: <span className="font-bold">{account.party.type}</span></div>
                        </div>
                    })
                }
            </section>            
        </>
    )
}
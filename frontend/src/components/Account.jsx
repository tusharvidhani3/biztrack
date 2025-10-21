import { useEffect, useState } from "react"
import { useParams } from "react-router"
import plusIcon from '../assets/icons/plus.svg'
import { useAuthFetch } from "../hooks/useAuthFetch"
import { apiBaseUrl } from "../config"
import loadingGif from '../assets/images/loading.gif'

export default function Account() {

    const { accountId } = useParams()

    const [account, setAccount] = useState(null)

    const { authFetch } = useAuthFetch()

    async function getAccount() {
        const res = await authFetch(`${apiBaseUrl}/api/accounts/${accountId}`, {
            method: 'GET'
        })
        const accountResponse = await res.json()
        setAccount(accountResponse)
    }

    useEffect(() => {
        const init = async () => await getAccount()
        init()
    }, [])

    return account ? (
        <>
            <div className="rounded-xl ">
                <div>{account.party.name}</div>

                <div className="flex flex-col">
                    <div>Outstanding Balance</div>
                    <div>{account.totalDue}</div>
                </div>

                <div className="flex flex-col">
                    <div>Next Closing Due</div>
                    <div>{account.nextClosingAmount}</div>
                </div>
            </div>

            <section className="flex flex-col">
                
            </section>

            <button className="fixed flex items-center justify-center right-4 bottom-4 bg-[#F1F1F1] rounded-xl h-10 w-10 cursor-pointer" ><img className="h-3/5" src={plusIcon} alt="add account entry" /></button>
        </>
    ) : <img className='loadingGif' src={loadingGif} alt="Loading..." />
}
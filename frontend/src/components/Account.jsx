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
                {account.entries.map(entry => {
                    const entryDate = new Date(entry.date)
                    if (entry.type === 'BILL') {
                        return <div className="flex"><div>{entryDate.getDate()} {entryDate.toLocaleString('default', { month: 'long' })} {entryDate.getFullYear()}</div> <div>₹{entry.amount}</div></div>
                    }
                    else {
                        return
                    }
                })}
            </section>

            <button className="fixed flex items-center justify-center right-4 bottom-4 bg-[#F1F1F1] rounded-xl h-10 w-10 cursor-pointer" ><img className="h-3/5" src={plusIcon} alt="add account entry" /></button>
        </>
    ) : <img className='loadingGif' src={loadingGif} alt="Loading..." />
}
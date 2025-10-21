import { useCallback, useEffect, useState } from "react";
import { useAuthFetch } from "../hooks/useAuthFetch";
import { apiBaseUrl } from '../config'
import GoodsTransactionCard from "./GoodsTransactionCard";
import prevIcon from '../assets/icons/prev.svg'
import nextIcon from '../assets/icons/next.svg'
import PaymentTransactionCard from "./PaymentTransactionCard";

export default function Transactions() {

    const authFetch = useAuthFetch()
    const [dayTransactions, setDayTransactions] = useState([])
    const [isTabIncoming, setTabIncoming] = useState(true)
    const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split("T")[0]) // If the user last opened any backdate transactions then shouw popup and ask to resume there, else open today's transactions page as default

    const getTransactions = useCallback(async () => {
        const res = await authFetch(`${apiBaseUrl}/api/transactions`, {
            method: 'GET'
        })
        const transactions = res.json()
        setDayTransactions(transactions)
    }, [])

    useEffect(() => {
        const init = async () => await getTransactions()
        init()
    }, [])

    return dayTransactions ? (
        <>
        <div className="flex gap-3 justify-center items-center">
            <button className="flex items-center justify-center cursor-pointer w-5" onClick={() => setSelectedDate(selectedDate => {
                const prevDate = new Date(selectedDate)
                prevDate.setDate(prevDate.getDate() - 1)
                return prevDate.toISOString().split("T")[0]
                })}><img src={prevIcon} alt="previous day" /></button>
            <input type="date" id="date" value={selectedDate} onChange={e => setSelectedDate(e.target.value)} />
            <button className="flex items-center justify-center cursor-pointer w-5" onClick={() => setSelectedDate(selectedDate => {
                const prevDate = new Date(selectedDate)
                prevDate.setDate(prevDate.getDate() + 1)
                return prevDate.toISOString().split("T")[0]
                })}><img src={nextIcon} alt="next day" /></button>
        </div>
        <nav className="flex w-full bg-amber-300">
            <button className={`basis-1/2 rounded m-1 cursor-pointer ${isTabIncoming ? 'bg-white cursor-default' : ''}`} onClick={() => setTabIncoming(true)}>Incoming</button>
            <button className={`basis-1/2 rounded m-1 cursor-pointer ${isTabIncoming ? '' : 'bg-white cursor-default'}`} onClick={() => setTabIncoming(false)}>Outgoing</button>
        </nav>
        {isTabIncoming ? 
        <section>
            {/* {dayTransactions.saleTransations.map(saleTransaction => <GoodsTransactionCard transaction={saleTransaction} />)} */}
            {/* {dayTransactions.sentPaymentTransactions.map(paymentTransaction => <PaymentTransactionCard transaction={paymentTransaction} />)} */}
        </section>
        :
        <section>
            {/* {dayTransactions.saleTransations.map(saleTransaction => <GoodsTransactionCard transaction={saleTransaction} />)} */}
            {/* {dayTransactions.sentPaymentTransactions.map(paymentTransaction => <PaymentTransactionCard transaction={paymentTransaction} />)} */}
        </section>
        }
        </>
    ) : 'loading...'
}
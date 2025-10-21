import { useCallback, useMemo, useReducer, useRef, useState } from "react"
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
        const accountsList = await res.json()
        setAccounts(accountsList)
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
            <Searchbox keyword={keyword} setKeyword={setKeyword} />
            <button className="fixed rounded"></button>
            <div className="flex rounded-3xl box-border w-9/10 bg-bg-surface px-4 py-1.5 gap-2 fixed bottom-2 h-10 max-w-full">
                <input className="" type="number" id="amount" step="0.1" inputMode="decimal" min="0" placeholder="Enter payment amount" value={paymentEntry.amount} onChange={e => dispatch({ type: 'AMOUNT', payload: e.target.value })} />
                <label htmlFor="date" onClick={() => dateInputRef.current.showPicker()}>{dateLabel}</label>
                <input ref={dateInputRef} className="hidden" type="date" id="date" value={paymentEntry.date} onChange={e => dispatch({ type: 'DATE', payload: e.target.value })} />
            </div>
        </>
    )
}
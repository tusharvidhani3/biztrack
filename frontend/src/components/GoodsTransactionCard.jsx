import { useReducer } from "react";
import { apiBaseUrl } from "../config";
import { useAuthFetch } from "../hooks/useAuthFetch"

export default function GoodsTransactionCard({ transaction }) {
    const { id, partyName, initiationDate, completionDate, paymentMethod, entries, note } = transaction



    const goodsTransactionReducer = useCallback((state, action) => {
            const { type, payload } = action
            switch (type) {
                case 'PARTY_NAME':
                    return { ...state, partyName: payload }
                case 'PAYMENT_METHOD':
                    return { ...state, numberOfBags: payload }
                case 'PRICE':
                    return { ...state, price: payload }
                case 'QUANTITY':
                    return { ...state, quantity: payload }
            }
        }, [])

    const goodsTransaction = useReducer(goodsTransactionReducer, {
        partyName: partyName,
        paymentMethod: paymentMethod
    })
    
    const authFetch = useAuthFetch()

    async function updateTransaction() {
        const res = await authFetch(`${apiBaseUrl}/api/transactions/${id}`, {
            method: 'PUT',
            body: JSON.stringify({
                
            })
        })

    }

    return (
        <div className="flex ">
            <div>{paymentMethod}</div>
            <h3 className="underline">{partyName}</h3>
            <div>{note}</div>
            <ul>{entries.map(entry => <li>
                <div>{entry.productName} - {entry.numberOfBags} @ {entry.price}</div>
                {entry.quantities.map((qty, i) => <><span>{qty}</span>{i < entry.quantities.length-1 && ', '}</>)}
            </li>)}</ul>
        </div>
    )
}
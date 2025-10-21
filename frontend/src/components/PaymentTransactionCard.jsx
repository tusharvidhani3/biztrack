export default function PaymentTransactionCard({ transaction }) {

    const { id, party, amount } = transaction

    return (
        <div>
            <h3>{party.name}</h3>
            <div>₹{amount.toLocaleString('en-IN')}</div>
        </div>
    )
}
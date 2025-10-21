export default function GoodsTransactionCard({ transaction }) {
    const { id, partyName, initiationDate, completionDate, paymentMethod, entries, note } = transaction
    return (
        <div className="flex ">
            <h3>{partyName}</h3>
            <div>{paymentMethod}</div>
            <div>{note}</div>
            <ul>{entries.map(entry => <li>
                {entry.productName} x {entry.numberOfBags}
                {/* {entry.} */}
            </li>)}</ul>
        </div>
    )
}
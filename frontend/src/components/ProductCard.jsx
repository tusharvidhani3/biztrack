export default function ProductCard({ product }) {

    return (
        <div className="flex flex-col font-bold rounded-xl">
            <div>{product.name}</div>
            <div>Last Sell Price: <span className="font-bold">{product.lastSellPrice}</span></div>
            <div>Last Purchase Price: <span className="font-bold">{product.lastPurchasePrice}</span></div>
        </div>
    )
}
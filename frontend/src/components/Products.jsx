import { useCallback, useEffect, useState } from "react";
import { useAuthFetch } from "../hooks/useAuthFetch";
import { apiBaseUrl } from "../config";
import ProductCard from "./ProductCard";

export default function Products() {
    
    const authFetch = useAuthFetch()

    const [products, setProducts] = useState(null)
    const [keyword, setKeyword] = useState("")

    const searchProducts = useCallback(async () => {
        const res = await authFetch(`${apiBaseUrl}/api/products?keyword=${keyword}`, {
            method: 'GET'
        })
        const productsResponse = await res.json()
        setProducts(productsResponse)
    }, [])

    useEffect(() => {
        const init = async () => await searchProducts()
        init()
    }, [])

    return (
        <section className="flex flex-col">
            {products.map(product => <ProductCard product={product} />)}
        </section>
    )
}
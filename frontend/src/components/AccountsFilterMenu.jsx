import { useCallback, useReducer } from "react"

export default function AccountsFilterMenu({ searchParams, setSearchParams }) {


    const searchFilterReducer = useCallback((state, action) => {
        const { type, payload } = action

        switch (type) {
            case 'PARTY_TYPE':
                return { ...state, partyType: payload }
            case '':
                return { ...state, }
        }
    }, [])

    const [searchFilters, dispatch] = useReducer(searchFilterReducer, {
        partyType: searchParams.get('party-type') || '',
        places: searchParams.getAll('places') || new Set()
    })

    const applyFilters = useCallback(() => {
        const filters = []
        if (searchFilters.partyType)
            filters.push(`party-type=${searchFilters.partyType}`)
        if (searchFilters.places.length) {
            searchFilters.places.forEach(place => {
                filters.push(`places=${place}`)
            })
        }
    })

    return (
        <div>
            <div>
                <div>
                    <input type="radio" id="party-any" checked={searchFilters.partyType === ''} onChange={() => dispatch({ type: 'PARTY_TYPE', payload: '' })} />
                    <label htmlFor=""></label>
                </div>
                <div>
                    <input type="radio" id="party-customer" checked={searchFilters.partyType === 'CUSTOMER'} onChange={() => dispatch({ type: 'PARTY_TYPE', payload: 'CUSTOMER' })} />
                    <label htmlFor="party-customer"></label>
                </div>
                <div>
                    <input type="radio" id="party-supplier" checked={searchFilters.partyType === 'SUPPLIER'} onChange={() => dispatch({ type: 'PARTY_TYPE', payload: 'SUPPLIER' })} />
                    <label htmlFor="party-supplier"></label>
                </div>
            </div>

            <div>
                <button onClick={() => setSearchParams(prev => {
                    const params = new URLSearchParams(prev)
                    if(searchFilters.partyType)
                        params.set('party-type', searchFilters.partyType)
                    if(searchFilters.places) {
                        searchFilters.places.forEach(place => params.append('places', place))
                    }
                    return params
                })}>Apply</button>
            </div>
        </div>
    )
}
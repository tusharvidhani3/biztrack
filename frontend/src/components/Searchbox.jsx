import searchIcon from '../assets/icons/search.svg'

export default function Searchbox({ keyword, setKeyword, handleSubmit, placeholder, width }) {

    return (
        <form className={`flex items-center w-4/5 bg-bg-surface gap-1 h-12 rounded-3xl overflow-hidden px-4`}>
            <input className='h-4/5 grow' type="text" id="searchbox" placeholder={placeholder} value={keyword} onChange={e => setKeyword(e.target.value)} />
            <button className='cursor-pointer' onClick={e => {
                e.preventDefault()
                handleSubmit()
            }}><img src={searchIcon} alt="search" /></button>
        </form>
    )
}
import hamburgerIcon from'../assets/icons/hamburger-icon.svg'

export default function Header({ setSideMenuOpen }) {
    return (
        <header className='h-12 sticky top-0 left-0 px-3 bg-inherit flex items-center w-full'>
            <button className='flex items-center justify-center h-8 bg-inherit cursor-pointer' onClick={() => setSideMenuOpen(true)}><img src={hamburgerIcon} alt="side menu toggle" className='h-full' /></button>
        </header>
    )
}
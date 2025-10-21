import { NavLink } from "react-router";

export default function SideMenu({ isSideMenuOpen, setSideMenuOpen }) {

    return (
        <>
            {isSideMenuOpen && <div className="fixed inset-0 bg-[rgba(0,0,0,0.6)] z-2 pointer-events-auto h-screen overflow-hidden" onClick={() => setSideMenuOpen(false)} />}
            <aside className={`fixed top-0 left-0 flex flex-col h-full z-3 transition-transform delay-200 ease-in-out bg-[#F9F9F9] w-56 ${isSideMenuOpen ? '' : '-translate-x-full'}`} onClick={e => {
                e.stopPropagation()
                if (e.target.closest('a') || e.target.closest('button'))
                    setSideMenuOpen(false)
            }}>
                <nav className="flex flex-col w-full gap-3">
                    <NavLink to={'/'} className={`h-8 flex items-center px-4 text-black hover:bg-[#EFEFEF] ${({ isActive }) => isActive ? 'bg-gray-300' : ''}`}>Dashboard</NavLink>
                    <NavLink to={'/transactions'} className={`h-8 flex items-center px-4 hover:bg-[#EFEFEF] ${({ isActive }) => isActive ? 'bg-gray-300' : ''}`}>Transactions</NavLink>
                    <NavLink to={'/accounts'} className={`h-8 flex items-center px-4 hover:bg-[#EFEFEF] ${({ isActive }) => isActive ? 'bg-gray-300' : ''}`}>Accounts</NavLink>
                    <NavLink to={'/products'} className={`h-8 flex items-center px-4 hover:bg-[#EFEFEF] ${({ isActive }) => isActive ? 'bg-gray-300' : ''}`}>Products</NavLink>
                    <NavLink to={'/customers'} className={`h-8 flex items-center px-4 hover:bg-[#EFEFEF] ${({ isActive }) => isActive ? 'bg-gray-300' : ''}`}>Customers</NavLink>
                    <NavLink to={'/suppliers'} className={`h-8 flex items-center px-4 hover:bg-[#EFEFEF] ${({ isActive }) => isActive ? 'bg-gray-300' : ''}`}>Suppliers</NavLink>
                    <NavLink to={''} className={`h-8 flex items-center px-4 hover:bg-[#EFEFEF] ${({ isActive }) => isActive ? 'bg-gray-300' : ''}`}>Profile</NavLink>
                </nav>
            </aside>
        </>
    )
}
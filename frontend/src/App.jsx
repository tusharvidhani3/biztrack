import { useState } from 'react'
import './App.css'
import SideMenu from './components/SideMenu'
import { Outlet } from 'react-router'
import Header from './components/Header'
import { UserProvider } from './contexts/UserContext'
import { ErrorProvider } from './contexts/ErrorContext'

function App() {

  const [isSideMenuOpen, setSideMenuOpen] = useState(false)

  return (
    <ErrorProvider>
      <UserProvider>
        <SideMenu isSideMenuOpen={isSideMenuOpen} setSideMenuOpen={setSideMenuOpen} />
        <Header setSideMenuOpen={setSideMenuOpen} />
        <main className='flex flex-col items-center'>
          <Outlet />
        </main>
      </UserProvider>
    </ErrorProvider>
  )
}

export default App

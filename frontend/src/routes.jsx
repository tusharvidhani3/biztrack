import { createBrowserRouter } from "react-router";
import App from "./App";
import Accounts from "./components/Accounts";
import Account from "./components/Account";
import Transactions from "./components/Transactions";
import Dashboard from "./components/Dashboard";
import Products from "./components/Products";
import Product from "./components/Product";

const router = createBrowserRouter([
    {
        path: '/',
        element: <App />,
        children: [
            {
                index: true,
                element: <Dashboard />
            },

            {
                path: 'transactions',
                element: <Transactions />
            },

            {
                path: 'accounts',
                children: [
                    {
                        index: true,
                        element: <Accounts />
                    },

                    {
                        path: ':accountId',
                        element: <Account />
                    }
                ]
            },

            {
                path: 'products',
                children: [
                    {
                        index: true,
                        element: <Products />
                    },

                    {
                        path: ':productId',
                        element: <Product />
                    }
                ]
            }
        ]
    }
])

export default router
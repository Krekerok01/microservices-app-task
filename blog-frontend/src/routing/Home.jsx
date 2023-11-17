import { BrowserRouter, Route, Routes } from "react-router-dom"
import AllComponents from "../pages/AllComponents";
import FormPost from "../pages/FormPost";

const Home = () => {
    return <BrowserRouter>
        <Routes>
            <Route element={<AllComponents/>} path='/' />
            <Route element={<FormPost />} path='/addPost' />


            <Route path="*" element={<div>Error!</div>} />
        </Routes>
    </BrowserRouter>
}


export default Home
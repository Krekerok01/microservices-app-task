import {BrowserRouter, Route, Routes} from "react-router-dom"
import AllComponents from "../pages/AllComponents";
import FormPost from "../pages/FormPost";
import LoginForm from "../pages/LoginForm";

const Home = () => {
    return <BrowserRouter>
        <Routes>
            <Route element={<AllComponents/>} path='/'/>
            <Route element={<FormPost/>} path='/addPost'/>
            <Route element={<LoginForm/>} path='/login'/>


            <Route path="*" element={<div>Error!</div>}/>
        </Routes>
    </BrowserRouter>
}


export default Home
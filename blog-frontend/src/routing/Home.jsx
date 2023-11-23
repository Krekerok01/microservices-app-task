import {BrowserRouter, Route, Routes} from "react-router-dom"
import AllComponents from "../pages/AllComponents";
import FormPost from "../pages/FormPost";
import SignUpForm from "../pages/SignUpForm";
import LoginForm from "../pages/LoginForm";
import UserPage from "../pages/UserPage";
import OneUser from "../pages/OneUser";
import UpdateFormPost from "../pages/UpdateFormPost";

const Home = () => {
    return <BrowserRouter>
        <Routes>
            <Route element={<AllComponents/>} path='/'/>
            <Route element={<FormPost/>} path='/addPost'/>
            <Route element={<SignUpForm/>} path='/signup'/>
            <Route element={<LoginForm/>} path='/login'/>
            <Route element={<UserPage/>} path='/users'/>
            <Route element={<OneUser/>} path='/myPage'/>
            <Route element={<UpdateFormPost/>} path='/updatePost'/>


            <Route path="*" element={<div>Error!</div>}/>
        </Routes>
    </BrowserRouter>
}


export default Home
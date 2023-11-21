import {BrowserRouter, Route, Routes} from "react-router-dom"
import AllComponents from "../pages/AllComponents";
import FormPost from "../pages/FormPost";
import SignUpForm from "../pages/SignUpForm";
import LoginForm from "../pages/LoginForm";
import UserPage from "../pages/UserPage";

const Home = () => {
    return <BrowserRouter>
        <Routes>
            <Route element={<AllComponents/>} path='/'/>
            <Route element={<FormPost/>} path='/addPost'/>
            <Route element={<SignUpForm/>} path='/signup'/>
            <Route element={<LoginForm/>} path='/login'/>
            <Route element={<UserPage/>} path='/users'/>


            <Route path="*" element={<div>Error!</div>}/>
        </Routes>
    </BrowserRouter>
}


export default Home
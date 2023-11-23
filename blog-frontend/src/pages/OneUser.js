import '../user.css'
import UserInfoComponent from "../component/UserInfoComponent";
import OneUserPostsComponent from "../component/OneUserPostsComponent";

const OneUser = () => {
    return <div>
        <UserInfoComponent/>
        <OneUserPostsComponent/>
    </div>
}

export default OneUser
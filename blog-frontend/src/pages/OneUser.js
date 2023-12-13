import '../user.css'
import UserInfoComponent from "../component/UserInfoComponent";
import OneUserPostsComponent from "../component/OneUserPostsComponent";

const OneUser = () => {
    return <div className="subscriptions-posts-container">
        <UserInfoComponent/>
        <OneUserPostsComponent/>
    </div>
}

export default OneUser
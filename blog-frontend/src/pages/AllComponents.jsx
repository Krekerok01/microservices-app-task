import PostsComponent from "../component/PostsComponent";
import JobsComponent from "../component/JobsComponent";
import NewsComponent from "../component/NewsComponent";
import UserInfoComponent from "../component/UserInfoComponent";

const AllComponents = () => {
    return <div className="container">
        <UserInfoComponent/>
        <JobsComponent/>
        <PostsComponent/>
        <NewsComponent/>
    </div>
}


export default AllComponents
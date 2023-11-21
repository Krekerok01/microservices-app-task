import PostsComponent from "../component/PostsComponent";
import JobsComponent from "../component/JobsComponent";
import NewsComponent from "../component/NewsComponent";

const AllComponents = () => {
    return <div className="container">
        <JobsComponent/>
        <PostsComponent/>
        <NewsComponent/>
    </div>
}

export default AllComponents
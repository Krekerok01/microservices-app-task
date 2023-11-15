import './App.css';
import PostsComponent from "./component/PostsComponent";
import NewsComponent from "./component/NewsComponent";
import JobsComponent from "./component/JobsComponent";

function App() {
    return (
         <div className="container">
              <JobsComponent/>
              <PostsComponent/>
              <NewsComponent/>
          </div>
    );

}

export default App;

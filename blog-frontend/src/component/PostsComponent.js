import React from "react";
import PostCard from "./PostCard";
import ApiClient from "../client/ApiClient";

class PostsComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: []
        }
    }

    componentDidMount(): void {
        this.refreshPostCards();
        setInterval(this.refreshPostCards.bind(this), 5000);
    }

    refreshPostCards() {
        this.getPostsData().then(json => {
                this.setState({
                    data: json
                });
            }
        )
    }

    getPostsData(): Promise {
        return ApiClient.posts().then(
            response => {
                if (response.ok) {
                    return response.json();
                } else {
                    return Promise.reject("Post-service: error response");
                }
            }
        )
    }

    render() {
        return <div className="card-grid">
            {this.state.data.map(post => {
                return <PostCard key={post.postId} username={post.username} title={post.title} text={post.text}/>
            })}
        </div>
    }
}

export default PostsComponent
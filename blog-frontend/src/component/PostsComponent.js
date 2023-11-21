import React from "react";
import PostCard from "./PostCard";
import '../form.css'
import ApiClient from "../client/ApiClient";

class PostsComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: [],
            flag: false,
            showForm: false
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
            <button id="button_sign_up" onClick={() => {
                window.location.href = '/signup';
            }} className="button-login" style={{right: '120px'}}>Sign up
            </button>
            <button id="button_log" onClick={() => {
                window.location.href = '/login';
            }} className="button-login">Log in
            </button>
            <button id="button_create" className="button-login" onClick={() => {
                window.location.href = '/addPost';
            }} style={{display: 'none', right: '250px'}}>Create post
            </button>
            {this.state.data.map(post => {
                return <PostCard key={post.postId} username={post.username} title={post.title} text={post.text}/>
            })}
        </div>
    }
}

export default PostsComponent
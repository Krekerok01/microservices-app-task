import React from "react";
import PostCard from "./PostCard";
import '../form.css'
import ApiClient from "../client/ApiClient";
import aaa from './data.json'

class OneUserPostsComponent extends React.Component {
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
        return ApiClient.userPosts().then(
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
            <button id="button_back" className="button-login" onClick={() => {
                window.history.back();
            }} style={{top: '300px', right: '1930px'}}>Back
            </button>
            <button id="btn_create" className="button-login" onClick={() => {
                window.location.href='/addPost';
            }}>Create post
            </button>
            {this.state.data.map(post => {
                return <PostCard key={post.postId} username={post.username} title={post.title} text={post.text}/>
            })}
        </div>
    }
}

export default OneUserPostsComponent
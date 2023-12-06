import React from "react";
import PostCard from "./PostCard";
import '../form.css'
import ApiClient from "../client/ApiClient";

class OneUserPostsComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: [],
            show: true
        }
    }

    componentDidMount(): void {
        this.refreshPostCards();
        setInterval(this.refreshPostCards.bind(this), 5000);
    }

    refreshPostCards() {
        this.getPostsData().then(json => {
                this.setState({
                    data: json,
                    show: true
                });
            }
        ).catch(() => {
            this.setState({
                show: false
            });
        });
    }

    getPostsData(): Promise {
        return ApiClient.userPosts().then(
            response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new DOMException()
                }
            }
        ).catch(() => {
            throw new DOMException()
        });
    }

    render() {
        let posts;
        if (this.state.show === true) {
            posts = this.state.data.map(post => {
                return <PostCard key={post.postId} username={post.username} title={post.title} text={post.text}
                                 postId={post.postId}/>
            });
        } else {
            posts = <div className="loading-container" style={{paddingTop: '800px'}}>
                <div className="loading-text">
                    <span>L</span>
                    <span>O</span>
                    <span>A</span>
                    <span>D</span>
                    <span>I</span>
                    <span>N</span>
                    <span>G</span>
                    <span style={{paddingLeft: '30px'}}></span>
                    <span>P</span>
                    <span>O</span>
                    <span>S</span>
                    <span>T</span>
                    <span>S</span>
                </div>
            </div>
        }
        return <div className="card-grid">
            <button id="button_back" className="button-login" onClick={() => {
                window.history.back();
            }} style={{top: '300px', right: '1930px'}}>Back
            </button>
            <button id="button_home" className="button-login" onClick={() => {
                window.location.href = '/';
            }} style={{top: '370px', right: '1930px'}}>Home
            </button>
            <button id="button_edit_profile" className="button-login" onClick={() => {
                window.location.href = '/myPage/edit';
            }} style={{right: '1730px'}}>Edit profile
            </button>
            <button id="button_all_users" className="button-login" onClick={() => {
                window.location.href = '/users';
            }} style={{right: '1600px'}}>All users
            </button>
            <button id="btn_create" className="button-login" onClick={() => {
                window.location.href = '/addPost';
            }}>Create post
            </button>
            {/*{this.state.data.map(post => {*/}
            {/*    return <PostCard key={post.postId} username={post.username} title={post.title} text={post.text} postId={post.postId}/>*/}
            {/*})}*/}
            {posts}
        </div>
    }
}

export default OneUserPostsComponent
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
        ApiClient.posts().then(
            response => {
                if (response.ok) {
                    response.json().then(json => {
                        for (var i in json)
                            this.state.data.push([i, json[i]]);
                    })
                }
            }
        )
    }

    render() {
        return <div class="card-grid">
            {this.state.data.map(post => (
                    <PostCard title={post.title} text={post.text}/>
                )
            )}
        </div>
    }
}

export default PostsComponent
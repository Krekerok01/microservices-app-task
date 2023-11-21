import React from "react";
import '../form.css'
import ApiClient from "../client/ApiClient";
import UserCard from "./UserCard";

class UsersComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: []
        }
    }

    componentDidMount(): void {
        this.refreshUsers();
        setInterval(this.refreshUsers.bind(this), 5000);
    }

    refreshUsers() {
        this.getUsersData().then(json => {
                this.setState({
                    data: json
                });
            }
        )
    }

    getUsersData(): Promise {
        return ApiClient.users().then(
            response => {
                if (response.ok) {
                    return response.json();
                } else {
                    return Promise.reject("User-service: error response");
                }
            }
        )
    }

    render() {
        return <div className="card-grid">
            {this.state.data.map(user => {
                return <UserCard key={user.id} username={user.username} email={user.email}/>
            })}
        </div>
    }
}

export default UsersComponent
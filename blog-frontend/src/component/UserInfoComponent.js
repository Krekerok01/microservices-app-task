import React from "react";

class UserInfoComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: ""
        }
    }

    componentDidMount() {
        window.onscroll = function() {scrolling()};
        const header = document.getElementById('userHeader');
        const stickyStyle = header.offsetTop;
        function scrolling() {
            if(window.pageYOffset > stickyStyle) {
                header.classList.add(stickyStyle);
            } else {
                header.classList.remove(stickyStyle);
            }
        }
        const username = window.sessionStorage.getItem('username');
        if(username == null) {
            header.style.display = 'none';
        } else {
            console.log('Username: ' + username)
            this.setState({
                username: username
            });
        }
    }



    render() {
        return <div className="user-header" id="userHeader">
            <img src="https://th.bing.com/th/id/R.bae2d37c4317140a408aef6671346186?rik=X1vYbxH6nQxCcA&riu=http%3a%2f%2fcdn.onlinewebfonts.com%2fsvg%2fimg_218090.png&ehk=poXsiWmpbb3%2b%2bK%2blj8H9AQprCYsoz4kt%2bU4rFFKbOCo%3d&risl=&pid=ImgRaw&r=0" alt="Image"/>
            <h2>{this.state.username}</h2>
        </div>
    }
}

export default UserInfoComponent
import '../App.css';

export default function UserCard(props) {
    const {username, email} = props;
    return (
        <div className="card">
            <h2>{username}</h2>
            <h3>{email}</h3>
        </div>
    );
}
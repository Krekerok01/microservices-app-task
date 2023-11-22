import '../user.css';

export default function UserCard(props) {
    const {username, email} = props;
    return (
        <article className="profile">
            <h2 className="profile-username">{username}</h2>
            <small className="profile-user-handle">{email}</small>
            <div className="profile-actions">
                <button className="btn btn--primary">Follow</button>
            </div>
        </article>
    );
}
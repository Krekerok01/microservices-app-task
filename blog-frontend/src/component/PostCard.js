import '../App.css';

export default function PostCard(props) {
    const {username, title, text} = props;
    return (
        <div className="card">
            <h2>{title}</h2>
            <h3>{text}</h3>
            <hr/>
            <h5><em>{username}</em></h5>
        </div>
    );
}
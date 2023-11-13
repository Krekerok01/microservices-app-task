import '../App.css';

export default function PostCard(props) {
    const {title, text} = props;
    return (
        <div class="card">
            <h2>{title}</h2>
            <h3>{text}</h3>
        </div>
    );
}
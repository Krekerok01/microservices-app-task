import '../App.css';

export default function PostCard(props) {
    const {sourceName, title, url, publishedAt} = props;
    return (
        <div class="news">
            <h6>{sourceName}</h6>
            <h5>{title}</h5>
            <a href={url} target="_blank" rel="noopener noreferrer">
                Read more
            </a>
            <h6>{publishedAt}</h6>
        </div>
    );
}
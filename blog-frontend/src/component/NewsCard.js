import '../App.css';

export default function NewsCard(props) {
    const {sourceName, title, url, publishedAt} = props;
    return (
        <div class="news">
            <h6>{sourceName}</h6>
            <h5>
                <a href={url} target="_blank" rel="noopener noreferrer">
                    {title}...
                </a>
            </h5>
            <h6>{publishedAt}</h6>
        </div>
    );
}
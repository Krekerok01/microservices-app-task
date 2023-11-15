import '../App.css';

export default function NewsCard(props) {
    const {sourceName, title, url, publishedAt} = props;
    return (
        <div class="news">
            <h6>
                <a href={url} target="_blank" rel="noopener noreferrer">
                    {title}...
                </a>
            </h6>
            <h6><small><em>{sourceName}</em></small></h6>
            <h6><small><em>{publishedAt}</em></small></h6>
        </div>
    );
}
import '../App.css';

export default function JobCard(props) {

    const {title, url, companyName, jobSource} = props;
    return (
        <div className="job">
            <h6><strong>{companyName}</strong> is looking for</h6>
            <h6><strong>{title}</strong></h6>
            <h6>
                <a href={url} target="_blank" rel="noopener noreferrer">
                    Quick apply
                </a>
            </h6>
            <h6><small><em>Found on the {jobSource}</em></small></h6>
        </div>
    );
}
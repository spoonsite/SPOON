.submission-comment {
    width: 65%;
    margin: 10px 0;
}

.owner-submission-comment {
    cursor: pointer;
}

.owner-submission-comment:hover {
    background: #ccc;
}

.submission-comment-left {
    float: left;
}

.submission-comment-right {
    float: right;
}

.submission-comment-right .block-comment {
    background: #eee;
}

.submission-comment-left .block-comment {
    background: #d1f0ff;
}

.submission-comment-left .author-comment {
    text-align: left;
}

.submission-comment-right .author-comment {
    text-align: right;
}

.submission-comment-left .time-created-comment {
    text-align: left;
}

.submission-comment-right .time-created-comment {
    text-align: right;
}

.block-comment {
    border-radius: 15px;
    padding: 10px;
}

@keyframes commentfadein {
    0%   {opacity: 0.0;}
    100% {opacity: 1.0;}
}

.submission-new-comment {
    animation-name: commentfadein;
    animation-duration: 2s;
}
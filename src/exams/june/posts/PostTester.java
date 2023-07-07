package exams.june.posts;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

interface IComment {
    int totalLikes();

    void like(String commentId);

    String toStringIndented(int indent);

    void addComment(String commentId, IComment reply);

}

abstract class CommentBase implements IComment {
    String id;
    String username;
    String content;
    int likes = 0;

    public CommentBase(String id, String username, String content) {
        this.id = id;
        this.username = username;
        this.content = content;
    }

    String indentString(int indent) {
        return IntStream.rangeClosed(0, indent)
                .mapToObj(i -> "    ")
                .collect(Collectors.joining(""));
    }

    @Override
    public String toStringIndented(int indent) {
        String ind = indentString(indent);
        return String.format("%sComment: %s\n%sWritten by: %s\n%sLikes: %d", ind, this.content, ind, this.username, ind, this.likes);
    }

}

class Comment extends CommentBase {

    List<IComment> replies;

    public Comment(String id, String username, String content) {
        super(id, username, content);
        replies = new ArrayList<>();
    }

    @Override
    public int totalLikes() {
        return likes + replies.stream().mapToInt(IComment::totalLikes).sum();
    }

    @Override
    public void like(String commentId) {
        if (this.id.equals(commentId)) {
            ++likes;
        } else {
            replies.forEach(v -> v.like(commentId));
        }
    }

    @Override
    public String toStringIndented(int indent) {
        String result = super.toStringIndented(indent);
        if (replies.size() > 0) {
            return result + "\n" + replies.stream()
                    .sorted(Comparator.comparing(IComment::totalLikes).reversed())
                    .map(r -> r.toStringIndented(indent + 1))
                    .collect(Collectors.joining("\n"));
        }
        return result;

    }

    @Override
    public void addComment(String commentId, IComment reply) {
        if (this.id.equals(commentId)) {
            this.replies.add(reply);
        } else {
            this.replies.forEach(r -> r.addComment(commentId, reply));
        }
    }

}

class Post {
    String username;
    String postContent;

    List<IComment> comments;

    public Post(String username, String postContent) {
        this.username = username;
        this.postContent = postContent;
        comments = new ArrayList<>();
    }

    void addComment(String username, String commentId, String content, String replyToId) {
        if (replyToId == null) {
            comments.add(new Comment(commentId, username, content));
        } else {
            comments.forEach(c -> c.addComment(replyToId, new Comment(commentId, username, content)));
        }
    }

    void likeComment(String commentId) {
        comments.forEach(c -> c.like(commentId));
    }

    @Override
    public String toString() {
        comments.sort(Comparator.comparing(IComment::totalLikes).reversed());
        return String.format("Post: %s\nWritten by: %s\nComments:\n%s",
                this.postContent,
                this.username,
                this.comments.stream().map(c -> c.toStringIndented(1)).collect(Collectors.joining("\n"))
        );
    }
}


public class PostTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String postAuthor = sc.nextLine();
        String postContent = sc.nextLine();

        Post p = new Post(postAuthor, postContent);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(";");
            String testCase = parts[0];

            if (testCase.equals("addComment")) {
                String author = parts[1];
                String id = parts[2];
                String content = parts[3];
                String replyToId = null;
                if (parts.length == 5) {
                    replyToId = parts[4];
                }
                p.addComment(author, id, content, replyToId);
            } else if (testCase.equals("likes")) { //likes;1;2;3;4;1;1;1;1;1 example
                for (int i = 1; i < parts.length; i++) {
                    p.likeComment(parts[i]);
                }
            } else {
                System.out.println(p);
            }

        }
    }
}

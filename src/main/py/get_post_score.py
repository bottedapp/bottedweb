import difflib
import praw

class Sim:
    def __init__(self, username):
        self.reddit = praw.Reddit(
            client_id="GgPNctP2KQdth-iX6aMGUQ",
            client_secret="6zov1gDWJ8Ij60yH3L7q6N_LnPUZHA",
            user_agent="botted 0.0.1",
    )
        self.user = self.reddit.redditor(username)
    
    def similarities(self):
        Similarities = {}
        for Index, Post in enumerate(self.user.submissions.new(limit=20)):
            for CheckIndex, CheckComment in enumerate(self.user.submissions.new(limit=20)):
                if not Post.subreddit.display_name == CheckComment.subreddit.display_name:
                    continue
                if Index != CheckIndex:
                    if Post.subreddit.display_name in Similarities:
                        List = Similarities[Post.subreddit.display_name]
                        List.append(difflib.SequenceMatcher(None, Post.title, CheckComment.title).ratio())
                        Similarities[Post.subreddit.display_name] = List
                        continue
                    Similarities[Post.subreddit.display_name] = [difflib.SequenceMatcher(None, Post.title, CheckComment.title).ratio()]
        SimilaritiesValues = list(Similarities.values())
        TotalSimilarities = []
        for Index, Item in enumerate(Similarities):
            TotalSimilarities.append(sum(SimilaritiesValues[Index]) / len(SimilaritiesValues[Index]))
        try:
            score = sum(TotalSimilarities) / len(TotalSimilarities)
        except ZeroDivisionError:
            score = 0.0
        return score

posts = Sim("lightmare69")
score = posts.similarities()

print(score)
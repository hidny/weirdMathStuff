package tests;
import random.*;

//TODO: solve knot theory.

public class KingBesideQueen {

	public static int NOT_FOUND = -100000;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//confirmed by:
		//http://math.stackexchange.com/questions/456347/probability-of-2-cards-being-adjacent
		double oddsRightBeside = checkOddsKingBesideQueen(10000000, 0);
		System.out.println("Odds of king right beside queen: " + oddsRightBeside);

		double oddsRightBesideish = checkOddsKingBesideQueen(10000000, 1);
		System.out.println("Odds of king beside queen with 1 space in between allowed: " + oddsRightBesideish);
	}
	
	
	public static double checkOddsKingBesideQueen(int numTrials, int numSpacesAllowed) {
		 random.Deck deck = new random.RandomDeck();
		 

		 int lastKingSeen;
		 int lastQueenSeen;
		 int quartile = 1;
		 int numSuccess = 0;
		 
		 int tempCard;
		 
		 String arrayTest[] = new String[52];
		
		 int k;
		 
		 for(int i=0; i<numTrials; i++) {
			 
			 lastKingSeen = NOT_FOUND;
			 lastQueenSeen = NOT_FOUND;
			 deck.shuffle();
			 for(int j=0; j<52; j++) {
				 arrayTest[j] = "";
			 }
			 k = 0;
			 
			while(deck.hasCards()) {
				tempCard = deck.getNextCard();
				arrayTest[k] = DeckFunctions.getBaseNumber(tempCard) + "";
				k++;
				
				if(lastKingSeen  != NOT_FOUND) {
					lastKingSeen++;
				}
				if(lastQueenSeen  != NOT_FOUND) {
					lastQueenSeen++;
				}
				
				if(DeckFunctions.getBaseNumber(tempCard) == 'Q') {
					
					lastQueenSeen = 0;
					if(lastKingSeen != NOT_FOUND && lastKingSeen - 1 <= numSpacesAllowed) {
						numSuccess++;
						break;
					}
					 
				 } else if(DeckFunctions.getBaseNumber(tempCard) == 'K') {
					 lastKingSeen = 0;
					 if(lastQueenSeen != NOT_FOUND && lastQueenSeen - 1 <= numSpacesAllowed) {
						numSuccess++;
						break;
					}
				 }
			 }
			 
			 if(i > (quartile*numTrials)/4) {
				 quartile++;
				 System.out.println("Tentative after 1st quartile: " + (1.0*numSuccess)/(i * 1.0));
			 }
		 }
		 
		 return (1.0*numSuccess)/(numTrials * 1.0);
	}

}

//(independent result:)
//http://headinside.blogspot.ca/2012/11/my-2-cards-what-are-odds.html

/*
 * Grey Matters: Blog
HOME
ABOUT
CONTACT
DOWNLOADS
SOCIAL
MEMORY TOOLS
Blog
Mental Gym
Presentation
Videos
Store

 
4
My 2 Cards? What Are The Odds?
Published on Thursday, November 08, 2012 in innumeracy, math, playing cards, Scam School, videos
Scam School logoWe're going to jump all the way back to 2009, to the 60th episode of Scam School (At this writing, that was 183 episodes ago).

In that episode, host Brian Brushwood presented a scam whose odds sounded almost too good to be true. I'll investigate the actual odds in this post.

In episode 60 of Scam School, there are 2 scams that are taught. This post focuses on the “Playing The Odds” scam which starts at the 4:33 mark in the following video:



What are the chances of two named values being together in a deck of cards? Brian mentions his experience of the probabilities in his write-up:

Amazingly (and to just about everyone's disbelief), it seems that about 70% of the time, any two named values will just happen to be side by side in a shuffled deck of cards!

(by the way, math wizards: if you can figure out a way to calculate the exact odds on this, I'm all ears. After hours of playing with the numbers, I finally gave up and just did a brute force calculation: after 50 trials, I ended up averaging about a 70% success rate)
It's not easy to develop probability equations for this challenge. Just defining all the possible arrangements involved is a challenges. I don't doubt that this is why Brian gave up playing with the numbers, and turned to brute force calculations, otherwise known as the Monte Carlo method.

James Grime filmed a response video in which he explains the difficulty of calculating the odds via equations, and the result of his own Monte Carlo simulations:



The video shows a probability of 48.3%, and the information box in the video says that other experiments moved that closer to 48.6%.

After watching this video, I wrote and ran my own Monte Carlo simulations in jQuery. I had the computer mix the deck using this implementation of the modern Fisher–Yates shuffling algorithm, which a quick pencil-and-paper exercise will make clear.

After running 10 million trials of my own simulation, my results suggested a 48.63627% chance of succeeding, effectively the same 48.6% chance described above. In short, the person betting against the 2 values showing up next to each other will win roughly 51.4% of the time. With such a low probability of success, how did this bet manage to become popular?

The first thought I had about this was that perhaps it involved paying less than true odds. The odds of you winning this bet are roughly 1.056 to 1 against. In other words, as long as you can convince someone to bet at least $1.06 to every $1 you bet, you could still make money with this bet over the long term. That doesn't seem very likely.

Many bets hinge on a little wordplay. For example, there's a classic bet where you claim you can name the day someone was born, with an accuracy of plus or minus 3 days. Once they put up their money, you simply say “Wednesday,“ and take their money. Since every day of the week is plus or minus 3 days from Wednesday, you can't lose.

In a similar manner, perhaps we can use wordplay to give us a better margin of error for this bet. What if, instead of mentioning that the cards must be next to each other, the bet was that the two values would be within 1 card of each other? If the two cards show up right next to each other, as in the original bet, this sounds exactly like what you bet. In addition, it also covers the possibility of the 2 values showing up with 1 card between them.

I re-programmed my simulation to include the new possibilities, ran it another 10 million times, and came up with about a 73.6% chance of success, or odds of roughly 2.8 to 1 in favor of winning!

Brian's own test trials intrigue me. Assuming that he wound up winning 34 out of those 50 times, which seems reasonable given the “about 70%” phrasing, Wolfram|Alpha says there's only about a 0.44% chance, or odds of about 224 to 1 against winning 34 or more out of 50 such trials! As with any trials, though, long shots can and do happen.

Alternatively, that claim might be a scam...

SPREAD THE LOVE, SHARE OUR ARTICLE
Delicious Digg NewsVine RSS StumbleUpon Technorati Twitter
RELATED POSTS
Cards and Dice
Cheryl's Birthday Round-Up
Tackle That Math Problem!
Danica McKellar's Math Bites
Yet Still More Quick Snippets
POST DETAILS
Posted by Pi Guy on Nov 8, 2012
Labels: innumeracy, math, playing cards, Scam School, videos
4 RESPONSE TO MY 2 CARDS? WHAT ARE THE ODDS?

 
tom en elissa
3:51 AM
By logic I've calculated a probability of 16/26... Or roughly a liitle more of 60%...
There are 51 places where cards meet... And (51*52)/2 possible pairs... So 1/26 that 2 chosen cards align...
With 16 possible copples (4 queens, and 4 aces for example)... So in total 16/26 chance. An othere solution then in the article?

No doubt others have more knowledge of this topic, so where did I make a wrong assumption?

 
Pi Guy
9:01 AM
This problem is far more complex than it appears initially.

First, while there are 16 possible couples, not all of those couples are winning pairs. Your bet would be that a queen and an ace are next to each other. A queen next to a queen doesn't count as a win.

The proper way to work this out as an equation is to first list all the possible ways the cards could fall that affect the outcome of the bet:

1st arrangement) All the queens and all the aces are all separated by 1 or more cards. (Losing arrangement)

2nd arrangement) A pair of cards of the same value are together, and all the other queens and aces are separated by 1 or more cards. (Losing arrangement)

...
(too many possibilities to count snipped)
...

nth arrangement) The queens and aces all alternate with each other (Winning arrangement)

Once you have a complete list like this, you can either...

A) ...work out the probabilities for each of the winning arrangements individually, then add them up to get the probability of winning.

B) ...work out the probabilities for each of the losing arrangements individually, add them up to get the probability of losing, and then subtract that from 1 to get the probability of winning.

As you can see, this is a very complex problem, which is why I turned to simply programming a computer to play it 10 million times, and keeping track of how often wins occurred.

–––––––––––

Another way to see how difficult the calculations can truly be is to work through the following process, attempting to work out the odds of a queen and an ace NOT being together:

First, we need to jump to the first qualifying card (queen or ace), and the probability of that card is 8/52.

Once we've zeroed in on that card, the next card can only be among the remaining 51 cards (since we've already used up 1 card). The odds of the next card NOT being the other 4 cards of the chosen value are 47/51 (if the first card was a queen, this is the odds of the next one NOT being an ace, and vice-versa).

We multiply those together:

(8/52)*(47/51)

We're not done yet, because we have other cards to consider!

Next, we need to find one of the remaining cards of the value, and that's 7 out of the remaining 50, or 7/50, right?

Not so fast! Assuming the first card we looked at was a queen, the card next to it could be a queen, as well. There might be only 6 cards we need to check out of the remaining 50.

So, what do we multiply by for the next step, 6/50 or 7/50? That's where we run into the problem. We're only two cards in, and trying to calculate this way is already causing major problems.

This is WHY you have to list the possibilities first, and then work out their respective probabilities, as discussed above.

Theoretically, listing all those possibilities should be possible, but in practice, it would take a very long time to develop the complete list (even assuming you didn't make a mistake and forget some of the possibilities).

That's the power of simulating the game on a computer. Assuming you defined the game properly and effectively, just playing it repeatedly enough times gives you the real-world results you need!

 
Joseph Kisenwether
5:02 PM
It's not actually too hard to work out exactly. Assume the two values I picked are Jacks and Fives. Then First we will consider the locations of the Jacks. There are 52 choose 4 = 270725 ways that these can be positioned in the deck. In most of these arrangements, there will be 8 different cards next to them (one on each side of each Jack), but in some there will be fewer. For example, two Jacks could be next to each other, or could be separated by only one other card and have to share a neighbor.
But there are only, 270725 possibilities, so it's easy to write a computer program to check them all. 
You get the following results:
1 2
2 56
3 426
4 3920
5 12320
6 52030
7 66220
8 135751

Where the number on the left is the number of cards that neighbor a Jack and the number on the right is the count of different ways this can happen based on the distribution of the Jacks.
Then we consider each of these cases independently. In the 135751 arrangements in which there are 8 places next to Jacks, what is the probability that one (or more) of these is a 5?
It turns out to be easier to ask, what's the probability that *none* are. This turns out to be an example of the hypergeometric distribution. We want to count there cases where there are 0 successes given 8 draws from a pool of 48 cards in which 4 are successes. In other Words HYPGEOMDIST(0,8,4,48) in Excel. It comes out to 0.469678281, which remember is the probability of NOT getting a match. SO the probability of getting one is 1-.469... = 0.530321719.

Now we just to the same thing for every other row in the table:
1 2 0.083333333
2 56 0.161347518
3 426 0.234273821
4 3920 0.302338370
5 12320 0.365762154
6 52030 0.424761024
7 66220 0.479545688
8 135751 0.530321719

And take a weighted average based on the 2nd column.

Probability of a pair of random values being next to one another in a deck = 0.486279044.

 
Anonymous
9:39 AM
I just developed a small simulation and find that the odds of winning are around 75,6 % after approx. 15 million iterations.

Post a Comment

Newer PostOlder PostHome
Subscribe to: Post Comments (Atom)
SEARCH GREY MATTERS

Type to search...
Submit

 
TWITTER UPDATES

FIND PAST POSTS BY...
...TOPIC
...DATE
...RANKING
...VIEW
fun (604)
math (592)
memory (434)
videos (410)
self improvement (387)
software (257)
memory feats (241)
site features (228)
magic (224)
products (224)
puzzles (208)
books (175)
playing cards (148)
psychology (140)
downloads (135)
calendar (107)
Martin Gardner (87)
Pi (84)
magic squares (83)
snippets (83)
TV (77)
innumeracy (70)
mental math (66)
Scam School (62)
reviews (57)
Knight's Tour (55)
nim (42)
poetry (38)
money (37)
controversy (31)
Numb3rs (23)
Sudoku (23)
DVDs (21)
Carnival of Mathematics (17)
world record (11)
savant (7)
Math Bites (1)
Subscribe To
   Posts
   Comments
Labels
fun (604) math (592) memory (434) videos (410) self improvement (387) software (257) memory feats (241) site features (228) magic (224) products (224) puzzles (208) books (175) playing cards (148) psychology (140) downloads (135) calendar (107) Martin Gardner (87) Pi (84) magic squares (83) snippets (83) TV (77) innumeracy (70) mental math (66) Scam School (62) reviews (57) Knight's Tour (55) nim (42) poetry (38) money (37) controversy (31) Numb3rs (23) Sudoku (23) DVDs (21) Carnival of Mathematics (17) world record (11) savant (7) Math Bites (1)
About Us
Welcome! Grey Matters is my website. However, Grey Matters is not about me.

Grey Matters is a website about you.

Learning is always beneficial, but the Grey Matters philosophy is that learning can and should be fun.

While the great majority of the features are focused on fun aspects of mathematics and memorization, Grey Matters covers anything concerning learning and fun.

Please take a look around, explore, and enjoy!
Copyright © 2010 Grey Matters: Blog.Wordpress Theme by Paddsolutions. Blogger Template by Blogger Widgets Designed For Chethstudios.
*/

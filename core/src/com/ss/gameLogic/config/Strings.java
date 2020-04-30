package com.ss.gameLogic.config;

public class Strings {

  public static String[] tutorialVN = {
          "LUẬT CHƠI LIÊNG",
          "Trong mỗi ván chơi liêng thường quy định từ 2-6 người chơi. Mỗi người sẽ được chia 3 cây bài và dựa vào bài của mình để cược một số tiền nhất định.",
          "Ban đầu người chơi sẽ cược trước số tiền cơ bản mà bàn chơi quy định. Sau khi chia bài người chơi sẽ dựa vào bài của mình quyết định tố thêm tiền cược hay không. Lượt chơi sẽ bắt đầu từ nhà cái theo chiều kim đồng hồ. Đến lượt mình người chơi có 3 lựa chọn như sau:",
          "- Tố: Nếu như thấy bài mình đẹp và khả năng giành phần thắng cao thì người chơi có thể lựa chọn tố và cược thêm một số tiền nữa.",
          "- Theo: Nếu như người chơi trước tố bạn có quyền theo hoặc không theo.",
          "- Bỏ: Không tiếp tục thâm gia ván và chấp nhận mất toàn bộ số tiền đã cược ban đầu",
          "- Tất tay: Khi cảm thấy bài mình có cơ hội thắng cao bạn có thể đặt cược tất cả số tiền mình có.",
          "Sau khi mọi người đặt tiền cược, người nào có bộ bài mang giá trị cao nhất sẽ là người thắng cuộc.",
          "Lưu ý: Trong luật chơi bài Liêng, mỗi ván chỉ tố một vòng duy nhất.",
          "Ván chơi sẽ kết thúc khi không có ai tố thêm. Nếu cả ván không ai theo thì có nghĩa người tố sẽ chiến thắng và ván bài của người đó sẽ được giấu kín. Nếu ván bài còn ít nhất 2 người theo thì cả 2 người đó đều phải hạ bài để so điểm. Bài người chơi nào có điểm cao nhất sẽ chiến thắng.",
          "Cách phân loại thắng thua trong chơi liêng",
          "- Sáp: bộ bài gồm 3 cây cùng số từ 2 đến A trong đó bộ 2 nhỏ nhất và bộ A là lớn nhất.",
          "- Liêng: 3 cây có số liên tiếp được tính từ A đến K. Tuy nhiên, trong chơi liêng chấp nhận bộ liên A, 2, 3 và bộ Q, K, A. Thông thường những bộ có số to hơn thì sẽ được coi là mạnh hơn nhưng nếu có cùng bộ liêng thì cần phải so sánh về chất theo tứ tự: Cơ > rô > tép > bích.",
          "- Ảnh: 3 cây toàn đầu người ( J, Q, K ), và không tính là bộ liêng liên tiếp JQK. Nếu người chơi cùng ảnh thì so sánh chất để phân cao thấp.",
          "- Các trường hợp còn lại sẽ tính điểm dựa vào tổng điểm của 3 cây bài. Trong đó các cây từ 2 đến 10 được tính theo điểm tương ứng với số, cây A là 1 điểm, JQK là 10 điểm. Trong đó 9 là lớn nhất và 1 là nhỏ nhất. Nếu người chơi có cùng số điểm như nhau thì được coi là hòa."
  };


  public static String[] tutorialEN = {
          "LAW ON PLAYING",
          "In each game of holiness usually rules from 2 to 6 players. Each person will be dealt 3 cards and based on his cards to bet a certain amount.",
          "At first the player will bet before the basic amount that the table requires. After dealing the hand the player will base on his hand to decide whether to raise the bet or not. The game will start from the dealer clockwise. The player has his turn, in turn, to have 3 options as follows: ",
          "- Poker: If you find your card is beautiful and the ability to win is high, players can choose to raise and bet an additional amount of money.",
          "- Theo: If the previous player denies you have the right to follow or not follow.",
          "- Abandon: No more intensive play and accept the full loss of the original bet",
          "- All hands: When you feel you have a good chance of winning, you can bet all the money you have.",
          "After everyone places a bet, the one with the highest value deck will be the winner.",
          "Note: In the card game rules, each round only plays one round.",
          "The game will end when no one raises. If the whole game does not follow, then the whistleblower will win and his or her game will be hidden. If the game has at least 2 followers then both that person must take a hand to score. The player with the highest score wins. ",
          "The classification of victory and defeat in spiritual play",
          "- Wax: a deck of cards with 3 numbers of the same number from 2 to A in which the smallest 2 and the largest A are the largest.",
          "- Lieng: 3 consecutive numbers are counted from A to K. However, in playing sacred, it is acceptable for A, 2, 3 and Q, K, A. is considered to be stronger but if there is the same spiritual set then it is necessary to compare the substance in the order of four: Mechanical> checkered> clove> flange. ",
          "- Photo: 3 trees of the whole head (J, Q, K), and does not count as consecutive JQK spiritual sets. If the player plays the same image, compare the high and low stool.",
          "- The remaining cases will be calculated based on the total points of the 3 cards. In which trees from 2 to 10 are calculated according to the points corresponding to the number, tree A is 1 point, JQK is 10 points. Of which 9 is the largest and 1 is the smallest. If the player has the same score then it is considered a tie. "
  };
}

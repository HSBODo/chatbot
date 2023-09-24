package site.pointman.chatbot.dto.response.property.common;





public class Profile {
    private String nickname;
    private String imageUrl;
     /**
      * 이미지 사이즈는 180px X 180px 추천합니다.
      **/

    public Profile(String nickname) {
        this.nickname = nickname;
    }

    public Profile(String nickname, String imageUrl) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }
}

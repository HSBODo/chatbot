package site.pointman.chatbot.globalservice;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.product.dto.ProductImageDto;
import site.pointman.chatbot.utill.CustomStringUtils;
import site.pointman.chatbot.utill.UrlResourceDownloader;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3FileService {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public ProductImageDto uploadImages(List<String> imgUrlList, String firstValue, String secondValue, String dir){
        final String EXT = ".jpg";

        ProductImageDto productImageDto = new ProductImageDto();
        imgUrlList.forEach(imgUrl -> {
            final String fileName = CustomStringUtils.createImgFileName(firstValue,secondValue);

            UrlResourceDownloader urlResourceDownloader = new UrlResourceDownloader( dir+"/"+fileName+EXT);
            urlResourceDownloader.download(imgUrl);

            File file = new File(dir+"/"+fileName+EXT);
            String uploadReturnUrl = upload(file, dir);

            productImageDto.getImageUrls().add(uploadReturnUrl);
        });
        return productImageDto;
    }

    public String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)

        return uploadImageUrl;      // 업로드된 파일의 S3 URL 주소 반환
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)	// PublicRead 권한으로 업로드 됨
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        }else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

}

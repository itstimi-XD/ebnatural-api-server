package ebnatural.bizcurator.apiserver.service;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ebnatural.bizcurator.apiserver.common.exception.custom.CategoryNotFoundException;
import ebnatural.bizcurator.apiserver.common.exception.custom.ErrorCode;
import ebnatural.bizcurator.apiserver.common.exception.custom.InvalidDocumentTypeException;
import ebnatural.bizcurator.apiserver.common.util.MemberUtil;
import ebnatural.bizcurator.apiserver.domain.*;
import ebnatural.bizcurator.apiserver.domain.constant.DocumentType;
import ebnatural.bizcurator.apiserver.dto.ApplicationDto;
import ebnatural.bizcurator.apiserver.dto.DocumentChangeDto;
import ebnatural.bizcurator.apiserver.dto.MyPageDocumentDetailDto;
import ebnatural.bizcurator.apiserver.dto.MyPageDocumentDto;
import ebnatural.bizcurator.apiserver.dto.PurchaseMakeDocumentDto;
import ebnatural.bizcurator.apiserver.dto.SellDocumentDto;
import ebnatural.bizcurator.apiserver.dto.request.PurchaseMakeDocumentRequest;
import ebnatural.bizcurator.apiserver.dto.request.SellDocumentRequest;
import ebnatural.bizcurator.apiserver.repository.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DocumentService {
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final PurchaseDocumentRepository purchaseDocumentRepository;
    private final MakeDocumentRepository makeDocumentRepository;
    private final SellDocumentRepository sellDocumentRepository;
    private final PurposeCategoryRepository purposeCategoryRepository;
    private final S3ImageUploadService s3ImageUploadService;

    private final EntityManager entityManager;

    @Value("${cloud.aws.s3.purchase-document-dir}")
    private String purchaseDir;
    @Value("${cloud.aws.s3.make-document-dir}")
    private String makeDir;
    @Value("${cloud.aws.s3.sell-document-dir}")
    private String sellDir;

    public void postPurchaseMakeDocument(PurchaseMakeDocumentRequest docDto, MultipartFile image) {
        // todo: 시큐리티 설정 후 주석해제
        // Member member = memberRepository.findByUserId(MemberUtil.getMemberId());
        Member member = memberRepository.findByUserId(3L);

        if (docDto.getDocumentType().equals(String.valueOf(DocumentType.make))) {
            PurposeCategory purposeCategory = purposeCategoryRepository.findById(docDto.getCategory())
                    .orElseThrow(() -> new CategoryNotFoundException());
            String storedPath = s3ImageUploadService.uploadImage(makeDir, image);
            makeDocumentRepository.save(MakeDocument.of(member, docDto, purposeCategory, storedPath));
        } else if (docDto.getDocumentType().equals(String.valueOf(DocumentType.purchase))) {
            Category category = categoryRepository.findById(docDto.getCategory())
                    .orElseThrow(() -> new CategoryNotFoundException());
            String storedPath = s3ImageUploadService.uploadImage(purchaseDir, image);
            purchaseDocumentRepository.save(PurchaseDocument.of(member, docDto, category, storedPath));
        } else
            throw new InvalidDocumentTypeException(ErrorCode.INVALID_DOCUMENT_TYPE_EXCEPTION);
    }

    public void postSellDocument(SellDocumentRequest docDto, MultipartFile image) {
        // todo: 시큐리티 설정 후 주석해제
        // Member member = memberRepository.findByUserId(MemberUtil.getMemberId());
        Member member = memberRepository.findByUserId(3L);
        Category category = categoryRepository.findById(docDto.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException());
        String storedPath = s3ImageUploadService.uploadImage(sellDir, image);
        sellDocumentRepository.save(SellDocument.of(member, docDto, category, storedPath));
    }

    public List<PurchaseMakeDocumentDto> getPurchaseMakeDocuments(Long memberId) {
        List<PurchaseMakeDocumentDto> purchaseMakeDocumentDtoList = new ArrayList<>();
        List<PurchaseMakeDocumentDto> purchaseDocumentDtoList =
                memberId == null ?
                        Optional.of(purchaseDocumentRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                                .stream()
                                .map(entity -> PurchaseMakeDocumentDto.fromPurchase(entity))
                                .collect(Collectors.toList())).get()
                        :
                        Optional.of(purchaseDocumentRepository.findByMemberId(memberId)
                                .stream()
                                .map(entity -> PurchaseMakeDocumentDto.fromPurchase(entity))
                                .collect(Collectors.toList())).get();

        List<PurchaseMakeDocumentDto> makeDocumentDtoList =
                memberId == null ?
                        Optional.of(makeDocumentRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                                .stream()
                                .map(entity -> PurchaseMakeDocumentDto.fromMake(entity))
                                .collect(Collectors.toList())).get()
                        :
                        Optional.of(makeDocumentRepository.findByMemberId(memberId)
                                .stream()
                                .map(entity -> PurchaseMakeDocumentDto.fromMake(entity))
                                .collect(Collectors.toList())).get();


        if (!purchaseDocumentDtoList.isEmpty()) {
            purchaseMakeDocumentDtoList.addAll(purchaseDocumentDtoList);

            if (!makeDocumentDtoList.isEmpty())
                purchaseMakeDocumentDtoList.addAll(makeDocumentDtoList);
        }
        return purchaseMakeDocumentDtoList;
    }

    public List<SellDocumentDto> getSellDocuments(Long memberId) {
        List<SellDocumentDto> sellDocumentDtoList =
                memberId == null ?
                        Optional.of(sellDocumentRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                                .stream()
                                .map(entity -> SellDocumentDto.from(entity))
                                .collect(Collectors.toList())).get()
                        :
                        Optional.of(sellDocumentRepository.findByMemberId(memberId)
                                .stream()
                                .map(entity -> SellDocumentDto.from(entity))
                                .collect(Collectors.toList())).get();

        return sellDocumentDtoList;
    }

    public void updatePurchaseMakeDocument(Long documentId, PurchaseMakeDocumentRequest docDto, MultipartFile image) {
        // todo: 시큐리티 설정 후 주석해제
        // Member member = memberRepository.findByUserId(MemberUtil.getMemberId());
        Member member = memberRepository.findByUserId(3L);
        if (docDto.getDocumentType().equals(String.valueOf(DocumentType.make))) {
            PurposeCategory purposeCategory = purposeCategoryRepository.findById(docDto.getCategory())
                    .orElseThrow(() -> new CategoryNotFoundException());
            MakeDocument makeDocument = makeDocumentRepository.findById(documentId)
                    .orElseThrow(() -> new EntityNotFoundException());
            if (!image.isEmpty()) {
                String recentPath = makeDocument.getImageDirectory();
                String storedPath = s3ImageUploadService.uploadImage(makeDir, image);
                s3ImageUploadService.deleteFile(recentPath);
                docDto.setImageDirectory(storedPath);
            }
            makeDocument.update(member, purposeCategory, docDto);

        } else if (docDto.getDocumentType().equals(String.valueOf(DocumentType.purchase))) {
            Category category = categoryRepository.findById(docDto.getCategory())
                    .orElseThrow(() -> new CategoryNotFoundException());
            PurchaseDocument purchaseDocument = purchaseDocumentRepository.findById(documentId)
                    .orElseThrow(() -> new EntityNotFoundException());
            if (!image.isEmpty()) {
                String recentPath = purchaseDocument.getImageDirectory();
                String storedPath = s3ImageUploadService.uploadImage(makeDir, image);
                s3ImageUploadService.deleteFile(recentPath);
                docDto.setImageDirectory(storedPath);
            }
            purchaseDocument.update(member, category, docDto);
        } else
            throw new InvalidDocumentTypeException(ErrorCode.INVALID_DOCUMENT_TYPE_EXCEPTION);
    }

    public void updateSellDocument(Long documentId, SellDocumentRequest docDto, MultipartFile image) {
        // todo: 시큐리티 설정 후 주석해제
        // Member member = memberRepository.findByUserId(MemberUtil.getMemberId());
        Member member = memberRepository.findByUserId(3L);
        Category category = categoryRepository.findById(docDto.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException());
        SellDocument sellDocument = sellDocumentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException());
        if (!image.isEmpty()) {
            String recentPath = sellDocument.getImageDirectory();
            String storedPath = s3ImageUploadService.uploadImage(makeDir, image);
            s3ImageUploadService.deleteFile(recentPath);
            docDto.setImageDirectory(storedPath);
        }
        sellDocument.update(member, category, docDto);
    }

    /**
     * 내 의뢰 내역 조회
     *  - 정해진 날짜로 부터 의뢰서 작성 내림차순으로 정렬함
     * @param filterDays
     * @return
     */
    public List<MyPageDocumentDto> showMyDocumentList(Integer filterDays) {
        if (filterDays == null) {
            // 기본이 3개월로 되어있음
            filterDays = 90;
        }
        Long memberId = 3L;
        // todo: 시큐리티 설정 on되면 주석 해제
        //Long memberId = MemberUtil.getMemberId();

        List<Object> documentList = new ArrayList<>();
        documentList.addAll(sellDocumentRepository.findAllByAfterFilteredDate(memberId, LocalDateTime.now().minusDays(filterDays)));
        documentList.addAll(makeDocumentRepository.findAllByAfterFilteredDate(memberId, LocalDateTime.now().minusDays(filterDays)));
        documentList.addAll(purchaseDocumentRepository.findAllByAfterFilteredDate(memberId, LocalDateTime.now().minusDays(filterDays)));

        // Comparator를 사용하여 documentList를 createdAt 값의 내림차순으로 정렬
        Collections.sort(documentList, (obj1, obj2) -> {
            LocalDateTime createdAt1 = getCreatedAtFromDocumentObject(obj1);
            LocalDateTime createdAt2 = getCreatedAtFromDocumentObject(obj2);
            return createdAt2.compareTo(createdAt1);
        });

        List<MyPageDocumentDto> myPageDocumentDtoList = new ArrayList<>();
        for (Object document : documentList) {
            MyPageDocumentDto myPageDocumentDto = null;

           if(document instanceof SellDocument){
               myPageDocumentDto = MyPageDocumentDto.fromEntity((SellDocument) document);
           }
           else if(document instanceof MakeDocument){
               myPageDocumentDto = MyPageDocumentDto.fromEntity((MakeDocument) document);
           }
           else if(document instanceof PurchaseDocument){
               myPageDocumentDto = MyPageDocumentDto.fromEntity((PurchaseDocument) document);
           }
           else {
               continue;
           }
            myPageDocumentDtoList.add(myPageDocumentDto);
        }

        return myPageDocumentDtoList;
    }

    // createdAt 값을 가져오는 유틸리티 메서드
    private LocalDateTime getCreatedAtFromDocumentObject(Object document) {
        if (document instanceof SellDocument) {
            return ((SellDocument) document).getCreatedAt();
        } else if (document instanceof MakeDocument) {
            return ((MakeDocument) document).getCreatedAt();
        } else if (document instanceof PurchaseDocument) {
            return ((PurchaseDocument) document).getCreatedAt();
        } else {
            // 예외 처리 또는 기본값 반환
            throw new IllegalArgumentException();
        }
    }

    /**
     * 의뢰 내역 상세조회
     * @param requestId
     * @param documentType Sell, Purchase, Make 중 하나
     * @return
     */
    public MyPageDocumentDetailDto showMyDocumentDetail(Long requestId, String documentType) {
        Long memberId = 3L;
        // todo: 시큐리티 설정 on되면 주석 해제
        //Long memberId = MemberUtil.getMemberId();

        switch (documentType) {
            case "Sell":
            case "sell":
            {
                SellDocument sellDocument = sellDocumentRepository.findByMemberIdAndId(memberId, requestId)
                        .orElseThrow(() -> new EntityNotFoundException());
                return MyPageDocumentDetailDto.fromEntity(sellDocument);
            }

            case "Make":
            case "make":
            {
                MakeDocument makeDocument = makeDocumentRepository.findByMemberIdAndId(memberId, requestId)
                        .orElseThrow(() -> new EntityNotFoundException());
                return MyPageDocumentDetailDto.fromEntity(makeDocument);
            }

            case "Purchase":
            case "purchase":
            {
                PurchaseDocument purchaseDocument = purchaseDocumentRepository.findByMemberIdAndId(memberId, requestId)
                        .orElseThrow(() -> new EntityNotFoundException());
                return MyPageDocumentDetailDto.fromEntity(purchaseDocument);
            }

            default :
                throw new IllegalArgumentException();

        }
    }

    /**
     * 의뢰서 수정
     * @param requestId
     * @param documentType
     * @return
     */
    public void changeDocument(Long requestId, String documentType, DocumentChangeDto documentChangeDto, MultipartFile image){
        // todo: 시큐리티 설정 후 주석해제
        // Long memberId = MemberUtil.getMemberId();
        Long memberId = 3L;

        switch (documentType) {
            case "Sell":
            case "sell":
            {
                SellDocument sellDocument = sellDocumentRepository.findByMemberIdAndId(memberId, requestId).orElseThrow(() -> new EntityNotFoundException());
                if (!image.isEmpty()) {
                    String recentPath = sellDocument.getImageDirectory();
                    sellDocument.setImageDirectory(s3ImageUploadService.uploadImage(makeDir, image));
                    s3ImageUploadService.deleteFile(recentPath);
                }
                sellDocument.update(categoryRepository.findByName(documentChangeDto.getCategory()), documentChangeDto);
            }
            break;

            case "Make":
            case "make":
            {
                MakeDocument makeDocument = makeDocumentRepository.findByMemberIdAndId(memberId, requestId).orElseThrow(() -> new EntityNotFoundException());
                if (!image.isEmpty()) {
                    String recentPath = makeDocument.getImageDirectory();
                    makeDocument.setImageDirectory(s3ImageUploadService.uploadImage(makeDir, image));
                    s3ImageUploadService.deleteFile(recentPath);
                }
                PurposeCategory purposeCategory = purposeCategoryRepository.findByName(documentChangeDto.getCategory());
                makeDocument.update(purposeCategory, documentChangeDto);
            }
            break;

            case "Purchase":
            case "purchase":
            {
                PurchaseDocument purchaseDocument = purchaseDocumentRepository.findByMemberIdAndId(memberId, requestId)
                        .orElseThrow(() -> new EntityNotFoundException());
                if (!image.isEmpty()) {
                    String recentPath = purchaseDocument.getImageDirectory();
                    purchaseDocument.setImageDirectory(s3ImageUploadService.uploadImage(makeDir, image));
                    s3ImageUploadService.deleteFile(recentPath);
                }
                Category productCategory = categoryRepository.findByName(documentChangeDto.getCategory());
                purchaseDocument.update(productCategory, documentChangeDto);
            }
            break;

            default :
                throw new IllegalArgumentException();

        }
    }

}


package ebnatural.bizcurator.apiserver.controller;

import ebnatural.bizcurator.apiserver.dto.PurchaseMakeDocumentDto;
import ebnatural.bizcurator.apiserver.dto.SellDocumentDto;
import ebnatural.bizcurator.apiserver.dto.request.PurchaseMakeDocumentRequest;
import ebnatural.bizcurator.apiserver.dto.request.SellDocumentRequest;
import ebnatural.bizcurator.apiserver.dto.response.CommonResponse;
import ebnatural.bizcurator.apiserver.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/requests")
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping("/orders")
    public ResponseEntity<CommonResponse> postPurchaseMakeDocument (@Valid @RequestPart(value = "post") PurchaseMakeDocumentRequest docDto,
                                                                    @RequestPart(value = "image") MultipartFile image){
        documentService.postPurchaseMakeDocument(docDto, image);
        return CommonResponse.ok(HttpStatus.OK.value(), "post purchase or make document success");
    }

    @PostMapping("/partners")
    public ResponseEntity<CommonResponse> postSellDocument (@Valid @RequestPart(value = "post") SellDocumentRequest docDto,
                                                            @RequestPart(value = "image") MultipartFile image){
        documentService.postSellDocument(docDto, image);
        return CommonResponse.ok(HttpStatus.OK.value(), "post sell document success");
    }

    /**
     * 관리자만 조회 가능
     * @return
     */
    @GetMapping(value = {"/orders/{userid}", "/orders"})
    public ResponseEntity<CommonResponse> getPurchaseMakeDocuments(@PathVariable(name = "userid", required = false) Long userId){
        List<PurchaseMakeDocumentDto> purchaseMakeDocumentDtoList = documentService.getPurchaseMakeDocuments(userId);
        return CommonResponse.ok(HttpStatus.OK.value(), "get purchase make document is success",
                Map.of("requestList", purchaseMakeDocumentDtoList));
    }

    @GetMapping(value = {"/partners/{userid}", "/partners"})
    public ResponseEntity<CommonResponse> getSellDocuments(@PathVariable(name = "userid", required = false) Long userId){
        List<SellDocumentDto> sellDocumentDtoList = documentService.getSellDocuments(userId);
        return CommonResponse.ok(HttpStatus.OK.value(), "get sell document is success",
                Map.of("requestList", sellDocumentDtoList));
    }

    @PatchMapping("/orders/{documentId}")
    public ResponseEntity<CommonResponse> updatePurchaseMakeDocument(@PathVariable(name = "documentId", required = false) Long documentId,
                                                                     @Valid @RequestPart(value = "post") PurchaseMakeDocumentRequest docDto,
                                                                     @RequestPart(value = "image", required = false) MultipartFile image){
        documentService.updatePurchaseMakeDocument(documentId, docDto, image);
        return CommonResponse.ok(HttpStatus.OK.value(), "update purchase or make document is success");
    }

    @PatchMapping("/partners/{documentId}")
    public ResponseEntity<CommonResponse> updateSellDocument(@PathVariable(name = "documentId", required = false) Long documentId,
                                                                     @Valid @RequestPart(value = "post") SellDocumentRequest docDto,
                                                                     @RequestPart(value = "image", required = false) MultipartFile image){
        documentService.updateSellDocument(documentId, docDto, image);
        return CommonResponse.ok(HttpStatus.OK.value(), "update sell document is success");
    }
}

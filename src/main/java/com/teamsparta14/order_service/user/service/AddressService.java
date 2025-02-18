package com.teamsparta14.order_service.user.service;

import com.teamsparta14.order_service.global.exception.BaseException;
import com.teamsparta14.order_service.global.response.ApiResponse;
import com.teamsparta14.order_service.global.response.ResponseCode;
import com.teamsparta14.order_service.user.dto.AddressRequestDTO;
import com.teamsparta14.order_service.user.dto.AddressResponseDTO;
import com.teamsparta14.order_service.user.dto.CustomUserDetails;
import com.teamsparta14.order_service.user.entity.AddressEntity;
import com.teamsparta14.order_service.user.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    //배송지 정보 저장
    public void save_address(CustomUserDetails customUserDetails, AddressRequestDTO addressRequestDTO){
        ModelMapper modelMapper = new ModelMapper();
        String username = customUserDetails.getUsername();

        AddressEntity addressEntity = modelMapper.map(addressRequestDTO, AddressEntity.class);
        addressEntity.setUsername(username);

        addressRepository.save(addressEntity);
    }

    //배송지 정보 조회
    public ApiResponse<List<AddressResponseDTO>> read_address(CustomUserDetails customUserDetails){
        ModelMapper modelMapper = new ModelMapper();
        List<AddressResponseDTO> addressResponseDTOS = new ArrayList<>();

        String username = customUserDetails.getUsername();

        List<AddressEntity> addressEntities = addressRepository.findAllByUsername(username);

        for(AddressEntity addressEntity : addressEntities){
            AddressResponseDTO addressResponseDTO = modelMapper.map(addressEntity, AddressResponseDTO.class);
            addressResponseDTOS.add(addressResponseDTO);
        }

        return ApiResponse.success(addressResponseDTOS);
    }

    //배송지 정보 수정
    @Transactional
    public ApiResponse<String> update_address(CustomUserDetails customUserDetails, AddressRequestDTO addressRequestDTO,UUID address_id){
        String username = customUserDetails.getUsername();

        AddressEntity addressEntity = addressRepository.findById(address_id).orElseThrow(()-> new BaseException("이미 삭제되었거나 없는 배송지 입니다"));

        //배송지가 해당 유저의 것인지 아닌지 체크
        if(!username.equals(addressEntity.getUsername())){
            throw new BaseException(ResponseCode.BAD_REQUEST, "적절하지 않은 접근입니다. 유효한 아이디를 설정해주세요");
        }

        addressEntity.setAddress(addressRequestDTO.getAddress());
        addressEntity.setMemo(addressRequestDTO.getMemo());
        addressRepository.save(addressEntity);
        return ApiResponse.success("배송지를 성공적으로 수정하였습니다.");
    }

    //배송지 정보 삭제
    @Transactional
    public ApiResponse<String> delete_address(CustomUserDetails customUserDetails, UUID address_id){
        String username = customUserDetails.getUsername();
        AddressEntity addressEntity = addressRepository.findById(address_id).orElseThrow(() -> new BaseException("이미 삭제되었거나 없는 배송지 입니다."));

        //배송지가 해당 유저의 것인지 아닌지 체크
        if(!username.equals(addressEntity.getUsername())){
            throw new BaseException(ResponseCode.BAD_REQUEST, "적절하지 않은 접근입니다. 유효한 아이디를 설정해주세요");
        }

        addressRepository.delete(addressEntity);
        return ApiResponse.success( "회원 탈퇴가 처리 되었습니다");
    }

}

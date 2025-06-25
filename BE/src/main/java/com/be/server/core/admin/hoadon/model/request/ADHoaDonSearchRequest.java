package com.be.server.core.admin.hoadon.model.request;

import com.be.server.core.common.base.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ADHoaDonSearchRequest extends PageableRequest {

   private String q;

   private String idSP;
}

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { environment } from "./../../environments/environment";

@Injectable()
export class AuthService {
  public BASE_URL: string;
  public HOST_URL: string;
  private is_login: boolean = false;
  constructor(private http: HttpClient, private spinner: NgxSpinnerService) {
    this.HOST_URL = environment.baseUrl;
    this.BASE_URL = this.HOST_URL + 'rest/api/v1'

  }
  openSpinner() {
    this.spinner.show();
  }
  closeSpinner() {
    this.spinner.hide();
  }
  /**dddd
   * login: function used to autheticate the user
   *
   * @param usercredential
   */

  login(usercredential: any): Promise<any> {
    let url: string = `${this.HOST_URL}login`;
    return this.http.get(url, { headers: this.getHeader(usercredential) }).toPromise();
  }

  register(data: any): Promise<any> {
    let url: string = `${this.HOST_URL}register`;
    return this.http.post(url,data, { headers: this.getDefaultHeader() }).toPromise();
  }

  loginPin(usercredential: any): Promise<any> {
    let url: string = `${this.HOST_URL}login-pin`;
    return this.http.get(url, { headers: this.getHeader(usercredential) }).toPromise();
  }
  pinAvailable(): Promise<any> {
    let url: string = `${this.BASE_URL}/is-pin-available`;
    return this.http.get(url, { headers: this.getBarearHeader() }).toPromise();
  }

  pinLogin(serviceId: any): Promise<any> {
    let url: string = `${this.BASE_URL}/`+serviceId;
    return this.http.get(url).toPromise();
  }
  
  logout(): Promise<any> {
    let url: string = `${this.HOST_URL}signout`;
    return this.http.put(url, {}, { headers: this.getBarearHeader() }).toPromise();
  }
  fileUpload(fileDetails: any): Promise<any> {
    let url: string = `${this.BASE_URL}/upload`;
    return this.http.post(url, fileDetails).toPromise();
  }

  /**
     * validateToken: fucntion used to check the token is expired or not for reset password
     *
     * @param user
     *
     */

  validateToken(token: any): Promise<any> {
    let url: string = `${this.HOST_URL}validateToken/` + token;
    return this.http.get(url, { headers: this.getDefaultHeader() }).toPromise();
  }
  validateOtp(otp: any): Promise<any> {
    let url: string = `${this.HOST_URL}validate-otp?otp=` + otp;
    return this.http.get(url, { headers: this.getBarearHeader() }).toPromise();
  }
  generateOtp(): Promise<any> {
    let url: string = `${this.HOST_URL}generate-otp`;
    return this.http.get(url, { headers: this.getBarearHeader() }).toPromise();
  }
  /**
   * changepassword: fucntion used to change the password who logged in currently
   *
   * @param user
   *
   */
  changepassword(user: any): Promise<any> {
    let url: string = `${this.BASE_URL}/changepassword`;
    return this.http.post(url, user, { headers: this.getBarearHeader() }).toPromise();
  }
  resetPassword(email: string): Promise<any> {
    let url: string = `${this.HOST_URL}resetPassword?email=` + email;
    return this.http.post(url, {}, { headers: this.getDefaultHeader() }).toPromise();
  }
  savePassword(data: any): Promise<any> {
    let url: string = `${this.HOST_URL}savePassword`;
    return this.http.post(url, data, { headers: this.getDefaultHeader() }).toPromise();
  }
  createPassword(data: any): Promise<any> {
    let url: string = `${this.HOST_URL}userConfirm`;
    return this.http.post(url, data, { headers: this.getDefaultHeader() }).toPromise();
  }

  resendRegistrationToken(token: string): Promise<any> {
    let url: string = `${this.HOST_URL}resendRegistrationToken?token=` + token;
    return this.http.get(url, { headers: this.getDefaultHeader() }).toPromise();
  }
  validateResetPasswordToken(token: string): Promise<any> {
    let url: string = `${this.HOST_URL}validate_reset_password_token?token=` + token;
    return this.http.get(url, { headers: this.getDefaultHeader() }).toPromise();
  }
  validateUserActivationToken(token: string): Promise<any> {
    let url: string = `${this.HOST_URL}validate_user_actiation_token?token=` + token;
    return this.http.get(url, { headers: this.getDefaultHeader() }).toPromise();
  }

  updatePassword(password: any): Promise<any> {
    let url: string = `${this.HOST_URL}updatePassword`;
    return this.http.post(url, password, { headers: this.getBarearHeader() }).toPromise();
  }
  upload(formData: any): Promise<any> {
    let url: string = `${this.BASE_URL}/file/upload`;
    return this.http.post(url, formData, { headers: this.getBarearHeaderToFileUpload() }).toPromise();
  }
  upload_(formData: any): Promise<any> {
    let url: string = `${this.BASE_URL}/upload`;
    return this.http.post(url, formData, { headers: this.getHeaderToFileUpload() }).toPromise();
  }
  upload_multiple(formData: any): Promise<any> {
    let url: string = `${this.BASE_URL}/medical-report-upload?file`;
    return this.http.post(url, formData, { headers: this.getHeaderToFileUpload() }).toPromise();
  }
  uploadProdcut(formData: any): Promise<any> {
    let url: string = `${this.BASE_URL}/file/upload/prodcut-image`;
    return this.http.post(url, formData, { headers: this.getBarearHeaderToFileUpload() }).toPromise();
  }
  serviceRefrenceAttachment(formData: any): Promise<any> {
    let url: string = `${this.BASE_URL}/service/refrence/attachment`;
    return this.http.post(url, formData, { headers: this.getBarearHeaderToFileUpload() }).toPromise();
  }
  complaintRefrenceAttachment(formData: any): Promise<any> {
    let url: string = `${this.BASE_URL}/complaint/refrence/attachment`;
    return this.http.post(url, formData, { headers: this.getBarearHeaderToFileUpload() }).toPromise();
  }
  exportAsCSV(path: any): Promise<any> {
    let url: string = `${this.BASE_URL}/` + path;
    return this.http.get(url, { headers: this.getBarearHeaderForCSV() }).toPromise();
  }
  /**
   *
   * resetpassword: function used to reset the password with thier email id.
   * @param user
   *
   */
  forgotpassword(user: any): Promise<any> {
    let url: string = `${this.HOST_URL}resetPassword`;
    return this.http.post(url, user, { headers: this.getBarearHeader() }).toPromise();
  }

  /**
   * queryService: function used to query the record from  table with filter condition.
   *
   *
   * @param serviceId
   */
   queryService(serviceId: any, filter?: any) {
    //  let url: string = `${this.BASE_URL}/` + serviceId+'?filter='+JSON.stringify(filter);
    let url: string = `${this.BASE_URL}/` + serviceId;
    return this.http.get(encodeURI(url), { headers: this.getBarearHeader() }).toPromise();
  }

  query(serviceId: any, filter?: any)   {
    //  let url: string = `${this.BASE_URL}/` + serviceId+'?filter='+JSON.stringify(filter);
    let url: string = `${this.BASE_URL}/` + serviceId;
    return this.http
      .get(encodeURI(url),
       { headers: this.getBarearHeader(),
        observe :'events'
      });
  }



  queryServiceWithOutToken(serviceId: any, filter: any) {
    let url: string = `${this.BASE_URL}/` + serviceId;
    return this.http.get(encodeURI(url), { headers: this.getWithoutTokenHeader() }).toPromise();
  }
  queryServiceForAdvancedSearch(serviceId: any, Object: any) {
    let url: string = `${this.BASE_URL}/` + serviceId;
    return this.http.post(encodeURI(url), Object, { headers: this.getBarearHeader() }).toPromise();
  }
  /**
   * createService: function used create a new record into table.
   *
   * @param Object
   * @param serviceId
   */
  createService(serviceId: any, Object: any) {
    let url: string = `${this.BASE_URL}/` + serviceId;
    return this.http.post(encodeURI(url), Object, { headers: this.getBarearHeader() }).toPromise();
  }
  createServiceWithoutJWT(serviceId: any, Object: any) {
    let url: string = `${this.BASE_URL}/` + serviceId;
    return this.http.post(encodeURI(url), Object, { headers: this.getWithoutTokenHeader() }).toPromise();
  }
  createUnAuthService(serviceId: any, Object: any) {
    let url: string = `${this.BASE_URL}/` + serviceId;
    return this.http.post(encodeURI(url), Object, { headers: this.getWithoutTokenHeader() }).toPromise();
  }
  createServiceWtToken(serviceId: any, Object: any) {
    let url: string = `${this.HOST_URL}` + serviceId;
    return this.http.post(encodeURI(url), Object, { headers: this.getWithoutTokenHeader() }).toPromise();
  }
  createService_(serviceId: any, Object: any) {
    let url: string = `${this.BASE_URL}/` + serviceId;
    return this.http.post(encodeURI(url), Object, { headers: this.getBarearHeader() }).toPromise();
  }
  /**
   * updateService: function used to update a record in table with help of unique id
   *
   * @param Object
   * @param serviceId
   */
  updateService(serviceId: any, Object: any) {
    let url: string = `${this.BASE_URL}/` + serviceId;
    return this.http.put(encodeURI(url), Object, { headers: this.getBarearHeader() }).toPromise();
  }
  //Upload File
  /**
   *
   * deleteService: function used to delete a record from the table with help of unique id
   *
   * @param uniqueId
   * @param serviceId
   */
  deleteService(serviceId: any, uniqueId: any) {
    let url: string = `${this.BASE_URL}/` + serviceId + `/` + uniqueId;
    return this.http.delete(encodeURI(url), { headers: this.getBarearHeader() }).toPromise();
  }
  public set_loginstatus(loginstatus: any) {
    this.is_login = loginstatus;
  }
  public get_loginstatus(): boolean {
    return this.is_login;
  }
  /**
   * getHeader:
   *
   * @param usercredential
   *
   */
  public getHeader(usercredential: any): HttpHeaders {
    return new HttpHeaders({ 'Content-Type': 'application/json', 'channel': 'MOBILE', 'Authorization': 'Basic ' + usercredential });
  }
  public getDefaultHeader(): HttpHeaders {
    return new HttpHeaders({ 'Content-Type': 'application/json', 'channel': 'MOBILE' });
  }
  /**
   * getHeader:
   *
   */
  public getBarearHeader(): HttpHeaders {
    let token = localStorage.getItem('etraze_token');
    return new HttpHeaders({ 'Content-Type': 'application/json', 'channel': 'MOBILE', 'Authorization': 'Bearer ' + token });
  }
  /**
 * getWITHOUTTokenHeader:
 *
 */
  public getWithoutTokenHeader(): HttpHeaders {
    return new HttpHeaders({ 'Content-Type': 'application/json', 'channel': 'MOBILE', 'Access-Control-Allow-Origin': '*', 'Access-Control-Allow-Methods': 'GET, POST, PATCH, PUT, DELETE, OPTIONS' });
  }

  /**
   * getHeader:
   *
   */
  public getBarearHeaderForCSV(): HttpHeaders {
    return new HttpHeaders({ 'Content-Type': 'text/csv', 'channel': 'MOBILE', 'Authorization': 'Bearer token', 'Access-Control-Allow-Origin': '*', 'Access-Control-Allow-Methods': 'GET, POST, PATCH, PUT, DELETE, OPTIONS', 'Access-Control-Allow-Headers': 'Origin, Content-Type, X-Auth-Token' });
  }

  /**
  * getHeader:
  *
  */
  public getBarearHeaderToFileUpload(): HttpHeaders {
    return new HttpHeaders({ 'channel': 'MOBILE', 'Authorization': 'Bearer ' + localStorage.getItem('etraze_token') });
  }
  /**
* getHeader:
*
*/
  public getHeaderToFileUpload(): HttpHeaders {
    return new HttpHeaders({ 'channel': 'MOBILE', 'Authorization': 'Bearer ' + localStorage.getItem('etraze_token') });
  }

  roleVal:any=  localStorage.getItem("etraze_role");
  getUserRole(){
    let role:any = localStorage.getItem("etraze_role");
    this.roleVal = JSON.parse(role)
    return this.roleVal.roleName;
  }

  userOnb:any;
  getUserOn(){
    let user:any = localStorage.getItem("etraze_user");
    this.userOnb = JSON.parse(user)
    return this.userOnb.isUserOnboarded;
  }

  userOtp:any;
  getUserOtp(){
    let user:any = localStorage.getItem("etraze_user");
    this.userOtp = JSON.parse(user)
    return this.userOtp.otpVerified;
  }

}

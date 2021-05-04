import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {Equipment} from '../dto/equipment';
import {UserEquipment} from '../dto/user-equipment';

const baseUri = environment.backendUrl + '/equipment';

@Injectable({
  providedIn: 'root'
})
export class EquipmentService {

  private httpOption = {headers: new HttpHeaders({'Content-Type': 'application/json'})};

  constructor(private httpClient: HttpClient) { }

  /**
   * Gets all the worn equipments of an user
   * @param id of the user
   */
  getEquipmentWornByUserId(id: number): Observable<Equipment[]> {
    console.log('getEquipmentWornByUserId');
    return this.httpClient.get<Equipment[]>(baseUri + '/wornByUser/' + id);
  }

  /**
   * Gets the currently worn equipment of a specific type of an user
   * @param type of the worn equipment
   * @param id of the user
   */
  getEquipmentOfTypeWornByUserId(type: string, id: number): Observable<Equipment> {
    console.log('getEquipmentOfTypeWornByUserId');
    return this.httpClient.get<Equipment>(baseUri + '/type/' + type + '/wornByUser/' + id);
  }

  /**
   * Gets all the bought equipments of a type except the curently equipped item
   * @param type of the equipment
   * @param id of the user
   */
  getAvailableEquipmentToEquip(type: string, id: number): Observable<Equipment[]> {
    console.log('getAvailableEquipmentToEquip');
    return this.httpClient.get<Equipment[]>(baseUri + '/type/' + type + '/toEquipFor/' + id);
  }

  /**
   * Unequips an item that is currently worn by a user
   * @param id of the user
   * @param eq that should be unworn
   */
  unequip(id: number, eq: Equipment): Observable<boolean>{
    console.log('unequip');
    const unequipBody = JSON.stringify(eq);
    return this.httpClient.post<boolean>(baseUri + '/unequip/' + id, unequipBody, this.httpOption);
  }

  /**
   * Gets the quipment of the id
   * @param id of the equipment
   */
  getOneById(id: number): Observable<Equipment> {
    console.log('getOneById');
    return this.httpClient.get<Equipment>(baseUri + '/' + id);
  }

  /**
   * Get all the available equipment items to buy for a specific user and type
   * @param type of the equipment
   * @param id of the user
   */
  getAvailableEquipmentByTypeAndId(type: string, id: number): Observable<Equipment[]> {
    console.log('getAvailableEquipmentByTypeAndId');
    let availParams = new HttpParams();
    availParams = availParams.set('user', String(id));
    availParams = availParams.set('type', type);
    return this.httpClient.get<Equipment[]>(baseUri + '/shop?' + availParams);
  }

  /**
   * When the user buys a new equipment
   * @param userEquipment object of user and id to buy
   */
  buyNewEquipment(userEquipment: UserEquipment): Observable<Equipment> {
    console.log('buyNewEquipment');
    const buyBody = JSON.stringify(userEquipment);
    return this.httpClient.post<Equipment>(baseUri + '/buy', buyBody, this.httpOption);
  }

  /**
   * Equips an item
   * @param userId of the user that should be euqipped with the item
   * @param equipment that should be equipped
   */
  equipItem(userId: number, equipment: Equipment): Observable<Equipment> {
    console.log('equipItem');
    const equipBody = JSON.stringify(equipment);
    return this.httpClient.post<Equipment>(baseUri + '/equip/' + userId, equipBody, this.httpOption);
  }
}

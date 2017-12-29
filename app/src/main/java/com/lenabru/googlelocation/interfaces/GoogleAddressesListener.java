package com.lenabru.googlelocation.interfaces;

import com.lenabru.googlelocation.models.GoogleAddresses;

/**
 * Created by Lena Brusilovski on 29/12/2017.
 */

public interface GoogleAddressesListener {

    void onAddressReceived(GoogleAddresses addresses);
}

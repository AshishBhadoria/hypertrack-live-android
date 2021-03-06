
/*
The MIT License (MIT)

Copyright (c) 2015-2017 HyperTrack (http://hypertrack.com)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package io.hypertrack.sendeta.store;

import android.graphics.Bitmap;

import java.io.File;

import io.hypertrack.sendeta.callback.OnOnboardingImageUploadCallback;
import io.hypertrack.sendeta.model.HyperTrackLiveUser;

/**
 * Created by ulhas on 18/06/16.
 */
public class OnboardingManager {

    private static OnboardingManager sSharedManager = null;
    private HyperTrackLiveUser hyperTrackLiveUser;

    private OnboardingManager() {
        this.hyperTrackLiveUser = HyperTrackLiveUser.sharedHyperTrackLiveUser();
    }

    public static OnboardingManager sharedManager() {
        if (sSharedManager == null) {
            sSharedManager = new OnboardingManager();
        }

        return sSharedManager;
    }

    public HyperTrackLiveUser getUser() {
        return hyperTrackLiveUser;
    }

    public void uploadPhoto(final Bitmap oldProfileImage, final Bitmap updatedProfileImage,
                            final OnOnboardingImageUploadCallback callback) {
        File profileImage = this.hyperTrackLiveUser.getPhotoImage();

        if (profileImage != null && profileImage.length() > 0) {

            // Check if the profile image has changed from the existing one
            if (updatedProfileImage != null && updatedProfileImage.getByteCount() > 0
                    && !updatedProfileImage.sameAs(oldProfileImage)) {
                this.hyperTrackLiveUser.saveFileAsBitmap(profileImage);
                HyperTrackLiveUser.setHyperTrackLiveUser();
                if (callback != null) {
                    callback.onSuccess();
                }
            } else {
                // No need to upload Profile Image since there was no change in it
                if (callback != null) {
                    callback.onImageUploadNotNeeded();
                }
            }
        } else {
            if (callback != null) {
                callback.onError();
            }
        }
    }
}

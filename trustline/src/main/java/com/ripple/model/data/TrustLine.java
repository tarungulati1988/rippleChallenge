package com.ripple.model.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashSet;
import java.util.UUID;

@Getter
@Setter
@Component
public class TrustLine implements Serializable {
  private static TrustLine trustLine = null;
  private HashSet<UUID> accounts = new HashSet<>();

  private TrustLine() {
  }

  /**
   * Static method to fetch the singleton object for Categories.
   *
   * @return - Categories instance
   */
  synchronized public static TrustLine getInstance() {
    if (trustLine == null) {
      trustLine = new TrustLine();
    }
    return trustLine;
  }

  /**
   * Register the account to individual account registry
   * @param uuid - account to be registered
   */
  public void addAccount(UUID uuid) {
    this.accounts.add(uuid);
  }
}

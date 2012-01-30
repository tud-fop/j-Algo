<object-stream>
  <org.jalgo.module.em.data.StartParameters>
    <events>
      <org.jalgo.module.em.data.Event>
        <tuple>
          <int>1</int>
          <int>1</int>
        </tuple>
        <experimentVector>
          <int>2</int>
          <int>2</int>
        </experimentVector>
        <yield>
          <name>2-mal Z</name>
          <frequency>3.0</frequency>
          <elements class="sorted-set">
            <org.jalgo.module.em.data.Event reference="../../.."/>
          </elements>
        </yield>
      </org.jalgo.module.em.data.Event>
      <org.jalgo.module.em.data.Event>
        <tuple>
          <int>2</int>
          <int>1</int>
        </tuple>
        <experimentVector reference="../../org.jalgo.module.em.data.Event/experimentVector"/>
        <yield>
          <name>1-mal Z</name>
          <frequency>5.0</frequency>
          <elements class="sorted-set">
            <org.jalgo.module.em.data.Event>
              <tuple>
                <int>1</int>
                <int>2</int>
              </tuple>
              <experimentVector reference="../../../../../org.jalgo.module.em.data.Event/experimentVector"/>
              <yield reference="../../.."/>
            </org.jalgo.module.em.data.Event>
            <org.jalgo.module.em.data.Event reference="../../.."/>
          </elements>
        </yield>
      </org.jalgo.module.em.data.Event>
      <org.jalgo.module.em.data.Event>
        <tuple>
          <int>2</int>
          <int>2</int>
        </tuple>
        <experimentVector reference="../../org.jalgo.module.em.data.Event/experimentVector"/>
        <yield>
          <name>0-mal Z</name>
          <frequency>2.0</frequency>
          <elements class="sorted-set">
            <org.jalgo.module.em.data.Event reference="../../.."/>
          </elements>
        </yield>
      </org.jalgo.module.em.data.Event>
      <org.jalgo.module.em.data.Event reference="../org.jalgo.module.em.data.Event[2]/yield/elements/org.jalgo.module.em.data.Event"/>
    </events>
    <observations>
      <org.jalgo.module.em.data.Partition reference="../../events/org.jalgo.module.em.data.Event/yield"/>
      <org.jalgo.module.em.data.Partition reference="../../events/org.jalgo.module.em.data.Event[3]/yield"/>
      <org.jalgo.module.em.data.Partition reference="../../events/org.jalgo.module.em.data.Event[2]/yield"/>
    </observations>
    <p0EMState>
      <data>
        <org.jalgo.module.em.data.EMData>
          <p>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[2]"/>
              <double>0.2</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event"/>
              <double>0.2</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[3]"/>
              <double>0.3</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[2]/yield/elements/org.jalgo.module.em.data.Event"/>
              <double>0.3</double>
            </entry>
          </p>
          <pForPartition>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event/yield"/>
              <double>0.2</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event[3]/yield"/>
              <double>0.3</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event[2]/yield"/>
              <double>0.5</double>
            </entry>
          </pForPartition>
          <pForSingleEvent>
            <entry>
              <java.awt.Point>
                <x>2</x>
                <y>1</y>
              </java.awt.Point>
              <double>0.6</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>1</x>
                <y>1</y>
              </java.awt.Point>
              <double>0.4</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>2</x>
                <y>0</y>
              </java.awt.Point>
              <double>0.5</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>1</x>
                <y>0</y>
              </java.awt.Point>
              <double>0.5</double>
            </entry>
          </pForSingleEvent>
          <likelihood>-4.647817481888637</likelihood>
        </org.jalgo.module.em.data.EMData>
        <org.jalgo.module.em.data.EMData>
          <p>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[2]"/>
              <double>0.1111111111111111</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event"/>
              <double>0.2222222222222222</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[3]"/>
              <double>0.2222222222222222</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[2]/yield/elements/org.jalgo.module.em.data.Event"/>
              <double>0.4444444444444444</double>
            </entry>
          </p>
          <pForPartition>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event/yield"/>
              <double>0.2222222222222222</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event[3]/yield"/>
              <double>0.2222222222222222</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event[2]/yield"/>
              <double>0.5555555555555556</double>
            </entry>
          </pForPartition>
          <pForSingleEvent>
            <entry>
              <java.awt.Point>
                <x>2</x>
                <y>1</y>
              </java.awt.Point>
              <double>0.6666666666666666</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>1</x>
                <y>1</y>
              </java.awt.Point>
              <double>0.3333333333333333</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>2</x>
                <y>0</y>
              </java.awt.Point>
              <double>0.3333333333333333</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>1</x>
                <y>0</y>
              </java.awt.Point>
              <double>0.6666666666666666</double>
            </entry>
          </pForSingleEvent>
          <likelihood>-4.542425094393249</likelihood>
        </org.jalgo.module.em.data.EMData>
        <org.jalgo.module.em.data.EMData>
          <p>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[2]"/>
              <double>0.25</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event"/>
              <double>0.25</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[3]"/>
              <double>0.25</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[2]/yield/elements/org.jalgo.module.em.data.Event"/>
              <double>0.25</double>
            </entry>
          </p>
          <pForPartition>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event/yield"/>
              <double>0.25</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event[3]/yield"/>
              <double>0.25</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event[2]/yield"/>
              <double>0.5</double>
            </entry>
          </pForPartition>
          <pForSingleEvent>
            <entry>
              <java.awt.Point>
                <x>2</x>
                <y>1</y>
              </java.awt.Point>
              <double>0.5</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>1</x>
                <y>1</y>
              </java.awt.Point>
              <double>0.5</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>2</x>
                <y>0</y>
              </java.awt.Point>
              <double>0.5</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>1</x>
                <y>0</y>
              </java.awt.Point>
              <double>0.5</double>
            </entry>
          </pForSingleEvent>
          <likelihood>-4.515449934959718</likelihood>
        </org.jalgo.module.em.data.EMData>
      </data>
    </p0EMState>
    <experiment>
      <int>2</int>
      <int>2</int>
    </experiment>
    <frequencySum>10.0</frequencySum>
    <type>2</type>
    <par1>2</par1>
    <par2>2</par2>
  </org.jalgo.module.em.data.StartParameters>
</object-stream>